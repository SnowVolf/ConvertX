#!/usr/bin/env bash

set -Eeuo pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
GRADLEW="$ROOT_DIR/gradlew"
VERSION_FILE="$ROOT_DIR/app/version.properties"

DRIVE_REMOTE="${DRIVE_REMOTE:-gdrive}"
DRIVE_FOLDER="${DRIVE_FOLDER:-Convert X}"
APP_LABEL="${APP_LABEL:-Convert X}"
BUILD_VARIANT="release"
DATE_VALUE="$(date '+%d.%m.%Y')"
DRY_RUN=0
UPLOAD=1

usage() {
    cat <<'EOF'
Usage: scripts/build-and-upload-drive.sh [options]

Build an Android APK and upload it to Google Drive through rclone.

Options:
  --variant <release|debug|both>  Build variant (default: release)
  --date <DD.MM.YYYY>            Override the date in the uploaded filename
  --remote <name>                rclone remote name (default: gdrive)
  --folder <path>                Google Drive folder path (default: Convert X)
  --no-upload                    Build only; do not upload
  --dry-run                      Build and print upload commands without uploading
  -h, --help                     Show this help

Environment variables with the same names are also supported:
  DRIVE_REMOTE, DRIVE_FOLDER, APP_LABEL
EOF
}

die() {
    printf 'Error: %s\n' "$*" >&2
    exit 1
}

read_version_property() {
    local key="$1"
    awk -F= -v key="$key" '$1 == key { sub(/\r$/, "", $2); print $2; exit }' "$VERSION_FILE"
}

parse_args() {
    while (($# > 0)); do
        case "$1" in
            --variant)
                (($# >= 2)) || die "--variant requires a value"
                BUILD_VARIANT="$2"
                shift 2
                ;;
            --date)
                (($# >= 2)) || die "--date requires a value"
                DATE_VALUE="$2"
                shift 2
                ;;
            --remote)
                (($# >= 2)) || die "--remote requires a value"
                DRIVE_REMOTE="$2"
                shift 2
                ;;
            --folder)
                (($# >= 2)) || die "--folder requires a value"
                DRIVE_FOLDER="$2"
                shift 2
                ;;
            --no-upload)
                UPLOAD=0
                shift
                ;;
            --dry-run)
                DRY_RUN=1
                shift
                ;;
            -h|--help)
                usage
                exit 0
                ;;
            *)
                die "Unknown option: $1"
                ;;
        esac
    done
}

validate_configuration() {
    [[ -x "$GRADLEW" ]] || die "Gradle wrapper is not executable: $GRADLEW"
    [[ -r "$VERSION_FILE" ]] || die "Version file not found: $VERSION_FILE"

    case "$BUILD_VARIANT" in
        release|debug|both) ;;
        *) die "Unsupported variant '$BUILD_VARIANT'; use release, debug or both" ;;
    esac

    [[ "$DATE_VALUE" =~ ^[0-9]{2}\.[0-9]{2}\.[0-9]{4}$ ]] || \
        die "Date must have DD.MM.YYYY format: $DATE_VALUE"

    if ((UPLOAD == 1 && DRY_RUN == 0)); then
        command -v rclone >/dev/null 2>&1 || die \
            "rclone is required for upload; run 'rclone config' first"
        rclone listremotes | awk -v expected="${DRIVE_REMOTE}:" '$0 == expected { found = 1 } END { exit !found }' || \
            die "rclone remote '${DRIVE_REMOTE}:' was not found; run 'rclone config' or use --remote"
    fi
}

build_version() {
    local major minor patch
    major="$(read_version_property VERSION_MAJOR)"
    minor="$(read_version_property VERSION_MINOR)"
    patch="$(read_version_property VERSION_PATCH)"
    [[ -n "$major" && -n "$minor" && -n "$patch" ]] || \
        die "Could not read VERSION_MAJOR, VERSION_MINOR and VERSION_PATCH from $VERSION_FILE"
    printf '%s.%s.%s' "$major" "$minor" "$patch"
}

build_variants() {
    local tasks=()
    case "$BUILD_VARIANT" in
        release) tasks+=(":app:assembleRelease") ;;
        debug) tasks+=(":app:assembleDebug") ;;
        both) tasks+=(":app:assembleDebug" ":app:assembleRelease") ;;
    esac

    printf 'Building: %s\n' "${tasks[*]}"
    (cd "$ROOT_DIR" && "$GRADLEW" "${tasks[@]}")
}

apk_for_variant() {
    local variant="$1"
    local output_dir="$ROOT_DIR/app/build/outputs/apk/$variant"
    local apk

    if [[ "$variant" == release ]]; then
        for apk in "$output_dir/app-release-unsigned.apk" "$output_dir/app-release.apk"; do
            if [[ -f "$apk" ]]; then
                printf '%s' "$apk"
                return 0
            fi
        done
    else
        apk="$output_dir/app-$variant.apk"
        if [[ -f "$apk" ]]; then
            printf '%s' "$apk"
            return 0
        fi
    fi

    return 1
}

upload_variant() {
    local variant="$1"
    local apk_path output_name destination

    apk_path="$(apk_for_variant "$variant")" || die "APK for $variant was not found"
    output_name="${APP_LABEL} ${VERSION} (${DATE_VALUE}) ${variant}.apk"
    destination="${DRIVE_REMOTE}:${DRIVE_FOLDER}/${output_name}"

    printf 'Prepared: %s (%s bytes)\n' "$apk_path" "$(stat -c '%s' "$apk_path")"
    if ((UPLOAD == 0 || DRY_RUN == 1)); then
        printf 'Would upload to: %s\n' "$destination"
        return 0
    fi

    printf 'Uploading: %s\n' "$output_name"
    rclone copyto "$apk_path" "$destination" --no-traverse
    printf 'Uploaded: %s\n' "$destination"
}

main() {
    parse_args "$@"
    validate_configuration
    VERSION="$(build_version)"
    build_variants

    case "$BUILD_VARIANT" in
        release) upload_variant release ;;
        debug) upload_variant debug ;;
        both)
            upload_variant debug
            upload_variant release
            ;;
    esac
}

main "$@"
