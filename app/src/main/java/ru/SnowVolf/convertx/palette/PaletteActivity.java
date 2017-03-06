package ru.SnowVolf.convertx.palette;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.JavaGirlToast;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.utils.StringUtils;
import ru.SnowVolf.convertx.utils.SystemF;

@SuppressWarnings({"ResourceAsColor", "ResourceType", "deprecation"})
public class PaletteActivity extends AppCompatActivity {
    private Button red, pink, purple, deep_purple, indigo, blue, light_blue, cyan, teal,
           green, light_green, lime, yellow, amber, orange, deep_orange, brown, grey, blue_grey;
    private Button hex50, hex100, hex200, hex300, hex400, hex500, hex600, hex700, hex800, hex900,
            hexa100, hexa200, hexa400, hexa700;
    private TextView caption, code;
    //10 пробелов
    private String sp = "          ";
    private String c0 = "50";
    private String c1 = "100";
    private String c2 = "200";
    private String c3 = "300";
    private String c4 = "400";
    private String c5 = "500";
    private String c6 = "600";
    private String c7 = "700";
    private String c8 = "800";
    private String c9 = "900";
    private String ca1 = "a100";
    private String ca2 = "a200";
    private String ca4 = "a400";
    private String ca7 = "a700";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        if (preferences.getBoolean("Theme.Theme", true)) {
            preferences.edit().putBoolean("Theme.Theme", true).apply();
            SystemF.onActivityCreateSetTheme(this);
            setTheme(R.style.PaletteLight);
        } else {
            setTheme(R.style.PaletteDark);
            preferences.edit().putBoolean("Theme.Theme", false).apply();
        }
        setContentView(R.layout.activity_color_palette);
        InitMainViews();
        InitExtraViews();
        SetClicks();
        SetTypefaces();
        //чтоб не было зеленых букв на старте
        RedClick();
        SetExtraClicks();
    }

    public void InitMainViews(){
        caption = (TextView) findViewById(R.id.color_caption);
        code = (TextView) findViewById(R.id.color_code);
        red = (Button) findViewById(R.id.cred);
        pink = (Button) findViewById(R.id.cpink);
        purple = (Button) findViewById(R.id.cpurple);
        deep_purple = (Button) findViewById(R.id.cdeep_purple);
        indigo = (Button) findViewById(R.id.cindigo);
        blue = (Button) findViewById(R.id.cblue);
        light_blue = (Button) findViewById(R.id.clight_blue);
        cyan = (Button) findViewById(R.id.ccyan);
        teal = (Button) findViewById(R.id.cteal);
        green = (Button) findViewById(R.id.cgreen);
        light_green = (Button) findViewById(R.id.clight_green);
        lime = (Button) findViewById(R.id.clime);
        yellow = (Button) findViewById(R.id.cyellow);
        amber = (Button) findViewById(R.id.camber);
        orange = (Button) findViewById(R.id.corange);
        deep_orange = (Button) findViewById(R.id.cdeep_orange);
        brown = (Button) findViewById(R.id.cbrown);
        grey = (Button) findViewById(R.id.cgey);
        blue_grey = (Button) findViewById(R.id.cblue_grey);
    }

    public void InitExtraViews(){
        hex50 = (Button) findViewById(R.id.hex50);
        hex100 = (Button) findViewById(R.id.hex100);
        hex200 = (Button) findViewById(R.id.hex200);
        hex300 = (Button) findViewById(R.id.hex300);
        hex400 = (Button) findViewById(R.id.hex400);
        hex500 = (Button) findViewById(R.id.hex500);
        hex600 = (Button) findViewById(R.id.hex600);
        hex700 = (Button) findViewById(R.id.hex700);
        hex800 = (Button) findViewById(R.id.hex800);
        hex900 = (Button) findViewById(R.id.hex900);
        hexa100 = (Button) findViewById(R.id.hexa100);
        hexa200 = (Button) findViewById(R.id.hexa200);
        hexa400 = (Button) findViewById(R.id.hexa400);
        hexa700 = (Button) findViewById(R.id.hexa700);
    }

    public void SetClicks(){
        /**
         * Java8 - сахар
         */
        red.setOnClickListener(view -> RedClick());
        pink.setOnClickListener(view -> PinkClick());
        purple.setOnClickListener(view -> PurpleClick());
        deep_purple.setOnClickListener(view -> DeepPurpleClick());
        indigo.setOnClickListener(view -> IndigoClick());
        blue.setOnClickListener(view -> BlueClick());
        light_blue.setOnClickListener(view -> LightBlueClick());
        cyan.setOnClickListener(view -> CyanClick());
        teal.setOnClickListener(view -> TealClick());
        green.setOnClickListener(view -> GreenClick());
        light_green.setOnClickListener(view -> LightGreenClick());
        lime.setOnClickListener(view -> LimeClick());
        yellow.setOnClickListener(view -> YellowClick());
        amber.setOnClickListener(view -> AmberClick());
        orange.setOnClickListener(view -> OrangeClick());
        deep_orange.setOnClickListener(view -> DeepOrangeClick());
        brown.setOnClickListener(view -> BrownClick());
        grey.setOnClickListener(view -> GreyClick());
        blue_grey.setOnClickListener(view -> BlueGreyClick());
    }

    public void SetExtraClicks(){
            String hex50s = hex50.getText().toString().substring(12);//отсчитываем 12 символов
            hex50.setOnClickListener(view -> {
                code.setText(hex50s);
                Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
                StringUtils.copyToClipboard(this, hex50s);
            });
        String hex100s = hex100.getText().toString().substring(13);//отсчитываем 13 символов
        hex100.setOnClickListener(view -> {
            code.setText(hex100s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex100s);
        });
        String hex200s = hex200.getText().toString().substring(13);
        hex200.setOnClickListener(view -> {
            code.setText(hex200s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex200s);
        });
        String hex300s = hex300.getText().toString().substring(13);
        hex300.setOnClickListener(view -> {
            code.setText(hex300s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex300s);
        });
        String hex400s = hex400.getText().toString().substring(13);
        hex400.setOnClickListener(view -> {
            code.setText(hex400s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex400s);
        });
        String hex500s = hex500.getText().toString().substring(13);
        hex500.setOnClickListener(view -> {
            code.setText(hex500s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex500s);
        });
        String hex600s = hex600.getText().toString().substring(13);
        hex600.setOnClickListener(view -> {
            code.setText(hex600s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex600s);
        });
        String hex700s = hex700.getText().toString().substring(13);
        hex700.setOnClickListener(view -> {
            code.setText(hex700s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex700s);
        });
        String hex800s = hex800.getText().toString().substring(13);
        hex800.setOnClickListener(view -> {
            code.setText(hex800s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex800s);
        });
        String hex900s = hex900.getText().toString().substring(13);
        hex900.setOnClickListener(view -> {
            code.setText(hex900s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hex900s);
        });
        String hexa100s = hexa100.getText().toString().substring(14);//отсчитываем 14 символов
        hexa100.setOnClickListener(view -> {
            code.setText(hexa100s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hexa100s);
        });
        String hexa200s = hexa200.getText().toString().substring(14);
        hexa200.setOnClickListener(view -> {
            code.setText(hexa200s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hexa200s);
        });
        String hexa400s = hexa400.getText().toString().substring(14);
        hexa400.setOnClickListener(view -> {
            code.setText(hexa400s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hexa400s);
        });
        String hexa700s = hexa700.getText().toString().substring(14);
        hexa700.setOnClickListener(view -> {
            code.setText(hexa700s);
            Toasty.info(this, getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
            StringUtils.copyToClipboard(this, hexa700s);
        });
    }

    public void SetTypefaces(){
        Typeface Mono = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/RobotoMono-Regular.ttf");

        //Применяем наш шрифт
        caption.setTypeface(Mono);
        code.setTypeface(Mono);
        hex50.setTypeface(Mono);
        hex100.setTypeface(Mono);
        hex200.setTypeface(Mono);
        hex300.setTypeface(Mono);
        hex400.setTypeface(Mono);
        hex500.setTypeface(Mono);
        hex600.setTypeface(Mono);
        hex700.setTypeface(Mono);
        hex800.setTypeface(Mono);
        hex900.setTypeface(Mono);
        hexa100.setTypeface(Mono);
        hexa200.setTypeface(Mono);
        hexa400.setTypeface(Mono);
        hexa700.setTypeface(Mono);

    }

    public void SetDarkness(){
        //делаем черный тескст на слишком светлых кнопках, чтоб можно было различить текст
        //на самом деле он не черный, а какой-то серый. Хз почему так.
        hex400.setTextColor(Color.BLACK);
        hex500.setTextColor(Color.BLACK);
        hex600.setTextColor(Color.BLACK);
        hex700.setTextColor(Color.BLACK);
        hexa400.setTextColor(Color.BLACK);
        Log.i("ConvertX", "darker");
    }

    public void SetWhiteness(){
        //и так понятно
        hex400.setTextColor(Color.WHITE);
        hex500.setTextColor(Color.WHITE);
        hex600.setTextColor(Color.WHITE);
        hex700.setTextColor(Color.WHITE);
        hexa400.setTextColor(Color.WHITE);
        Log.i("ConvertX", "darker");
    }

    //чтоб не показывались когда не надо
    public void RunIVisibleHelper(){
        hexa100.setVisibility(View.GONE);
        hexa200.setVisibility(View.GONE);
        hexa400.setVisibility(View.GONE);
        hexa700.setVisibility(View.GONE);
    }

    //чтоб показывались когда надо
    public void RunVisibleHelper(){
        hexa100.setVisibility(View.VISIBLE);
        hexa200.setVisibility(View.VISIBLE);
        hexa400.setVisibility(View.VISIBLE);
        hexa700.setVisibility(View.VISIBLE);
    }

    public void RedClick(){
        RunVisibleHelper();
        SetWhiteness();
        caption.setText("Red");
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.red50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.red50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.red100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.red100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.red200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.red200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.red300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.red300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.red400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.red400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.red500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.red500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.red600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.red600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.red700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.red700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.red800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.red800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.red900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.red900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.reda100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.reda100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.reda200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.reda200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.reda400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.reda400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.reda700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.reda700));
    }

    public void PinkClick(){
        caption.setText("Pink");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.pink50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.pink50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.pink100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.pink100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.pink200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.pink200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.pink300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.pink300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.pink400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.pink400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.pink500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.pink500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.pink600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.pink600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.pink700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.pink700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.pink800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.pink800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.pink900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.pink900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.pinka100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.pinka100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.pinka200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.pinka200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.pinka400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.pinka400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.pinka700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.pinka700));
        SetExtraClicks();
    }

    public void PurpleClick(){
        caption.setText("Purple");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.purple50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.purple50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.purple100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.purple100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.purple200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.purple200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.purple300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.purple300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.purple400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.purple400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.purple500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.purple500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.purple600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.purple600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.purple700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.purple700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.purple800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.purple800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.purple900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.purple900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.purplea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.purplea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.purplea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.purplea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.purplea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.purplea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.purplea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.purplea700));
        SetExtraClicks();
    }

    public void DeepPurpleClick(){
        caption.setText("Deep Purple");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.deep_purple50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.deep_purple100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.deep_purple200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.deep_purple300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.deep_purple400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.deep_purple500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.deep_purple600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.deep_purple700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.deep_purple800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.deep_purple900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.deep_purple900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.deep_purplea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.deep_purplea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.deep_purplea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.deep_purplea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.deep_purplea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.deep_purplea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.deep_purplea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.deep_purplea700));
        SetExtraClicks();
    }

    public void IndigoClick(){
        caption.setText("Indigo");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.indigo50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.indigo50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.indigo100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.indigo100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.indigo200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.indigo200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.indigo300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.indigo300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.indigo400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.indigo400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.indigo500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.indigo500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.indigo600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.indigo600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.indigo700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.indigo700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.indigo800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.indigo800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.indigo900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.indigo900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.indigoa100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.indigoa100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.indigoa200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.indigoa200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.indigoa400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.indigoa400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.indigoa700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.indigoa700));
        SetExtraClicks();
    }

    public void BlueClick(){
        caption.setText("Blue");
        SetWhiteness();
        RunVisibleHelper();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.blue50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.blue50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.blue100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.blue100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.blue200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.blue200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.blue300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.blue300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.blue400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.blue400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.blue500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.blue500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.blue600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.blue600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.blue700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.blue700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.blue800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.blue800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.blue900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.blue900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.bluea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.bluea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.bluea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.bluea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.bluea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.bluea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.bluea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.bluea700));
        SetExtraClicks();
    }

    public void LightBlueClick(){
        caption.setText("Light-Blue");
        SetWhiteness();
        RunVisibleHelper();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.light_blue50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.light_blue50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.light_blue100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.light_blue100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.light_blue200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.light_blue200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.light_blue300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.light_blue300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.light_blue400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.light_blue400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.light_blue500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.light_blue500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.light_blue600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.light_blue600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.light_blue700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.light_blue700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.light_blue800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.light_blue800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.light_blue900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.light_blue900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.light_bluea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.light_bluea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.light_bluea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.light_bluea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.light_bluea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.light_bluea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.light_bluea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.light_bluea700));
        SetExtraClicks();
    }

    public void CyanClick(){
        caption.setText("Cyan");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.cyan50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.cyan50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.cyan100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.cyan100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.cyan200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.cyan200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.cyan300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.cyan300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.cyan400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.cyan400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.cyan500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.cyan500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.cyan600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.cyan600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.cyan700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.cyan700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.cyan800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.cyan800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.cyan900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.cyan900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.cyana100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.cyana100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.cyana200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.cyana200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.cyana400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.cyana400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.cyana700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.cyana700));
        SetExtraClicks();
    }

    public void TealClick(){
        caption.setText("Teal");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.teal50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.teal50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.teal100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.teal100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.teal200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.teal200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.teal300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.teal300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.teal400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.teal400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.teal500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.teal500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.teal600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.teal600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.teal700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.teal700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.teal800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.teal800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.teal900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.teal900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.teala100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.teala100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.teala200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.teala200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.teala400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.teala400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.teala700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.teala700));
        SetExtraClicks();
    }

    public void GreenClick(){
        caption.setText("Green");
        RunVisibleHelper();
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.green50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.green50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.green100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.green100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.green200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.green200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.green300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.green300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.green400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.green400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.green500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.green500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.green600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.green600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.green700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.green700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.green800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.green800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.green900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.green900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.greena100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.greena100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.greena200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.greena200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.greena400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.greena400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.greena700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.greena700));
        SetExtraClicks();
    }

    public void LightGreenClick(){
        RunVisibleHelper();
        SetWhiteness();
        caption.setText("Light-Green");
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.light_green50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.light_green50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.light_green100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.light_green100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.light_green200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.light_green200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.light_green300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.light_green300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.light_green400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.light_green400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.light_green500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.light_green500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.light_green600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.light_green600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.light_green700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.light_green700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.light_green800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.light_green800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.light_green900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.light_green900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.light_greena100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.light_greena100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.light_greena200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.light_greena200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.light_greena400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.light_greena400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.light_greena700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.light_greena700));
        SetExtraClicks();
    }

    public void LimeClick(){
        caption.setText("Lime");
        RunVisibleHelper();
        SetDarkness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.lime50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.lime50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.lime100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.lime100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.lime200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.lime200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.lime300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.lime300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.lime400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.lime400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.lime500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.lime500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.lime600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.lime600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.lime700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.lime700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.lime800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.lime800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.lime900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.lime900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.limea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.limea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.limea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.limea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.limea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.limea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.limea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.limea700));
        SetExtraClicks();
    }

    public void YellowClick(){
        caption.setText("Yellow");
        RunVisibleHelper();
        SetDarkness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.yellow50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.yellow50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.yellow100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.yellow100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.yellow200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.yellow200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.yellow300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.yellow300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.yellow400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.yellow400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.yellow500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.yellow500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.yellow600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.yellow600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.yellow700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.yellow700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.yellow800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.yellow800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.yellow900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.yellow900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.yellowa100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.yellowa100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.yellowa200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.yellowa200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.yellowa400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.yellowa400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.yellowa700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.yellowa700));
        SetExtraClicks();
    }

    public void AmberClick(){
        caption.setText("Amber");
        RunVisibleHelper();
        SetDarkness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.amber50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.amber50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.amber100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.amber100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.amber200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.amber200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.amber300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.amber300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.amber400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.amber400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.amber500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.amber500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.amber600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.amber600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.amber700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.amber700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.amber800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.amber800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.amber900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.amber900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.ambera100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.ambera100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.ambera200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.ambera200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.ambera400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.ambera400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.ambera700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.ambera700));
        SetExtraClicks();
    }

    public void OrangeClick(){
        caption.setText("Orange");
        RunVisibleHelper();
        SetDarkness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.orange50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.orange50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.orange100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.orange100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.orange200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.orange200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.orange300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.orange300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.orange400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.orange400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.orange500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.orange500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.orange600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.orange600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.orange700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.orange700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.orange800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.orange800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.orange900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.orange900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.orangea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.orangea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.orangea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.orangea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.orangea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.orangea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.orangea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.orangea700));
        SetExtraClicks();
    }

    public void DeepOrangeClick(){
        caption.setText("Deep Orange");
        RunVisibleHelper();
        SetDarkness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.deep_orange50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.deep_orange100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.deep_orange200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.deep_orange300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.deep_orange400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.deep_orange500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.deep_orange600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.deep_orange700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.deep_orange800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.deep_orange900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.deep_orange900));

        hexa100.getBackground().setColorFilter(getResources().getColor(R.color.deep_orangea100), PorterDuff.Mode.SRC_ATOP);
        hexa100.setText(ca1+sp+getText(R.color.deep_orangea100));

        hexa200.getBackground().setColorFilter(getResources().getColor(R.color.deep_orangea200), PorterDuff.Mode.SRC_ATOP);
        hexa200.setText(ca2+sp+getText(R.color.deep_orangea200));

        hexa400.getBackground().setColorFilter(getResources().getColor(R.color.deep_orangea400), PorterDuff.Mode.SRC_ATOP);
        hexa400.setText(ca4+sp+getText(R.color.deep_orangea400));

        hexa700.getBackground().setColorFilter(getResources().getColor(R.color.deep_orangea700), PorterDuff.Mode.SRC_ATOP);
        hexa700.setText(ca7+sp+getText(R.color.deep_orangea700));
        SetExtraClicks();
    }

    public void BrownClick(){
        caption.setText("Brown");
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.brown50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.brown50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.brown100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.brown100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.brown200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.brown200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.brown300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.brown300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.brown400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.brown400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.brown500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.brown500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.brown600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.brown600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.brown700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.brown700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.brown800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.brown800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.brown900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.brown900));
        SetExtraClicks();

       RunIVisibleHelper();
    }

    public void GreyClick(){
        caption.setText("Grey");
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.grey50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.grey50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.grey100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.grey100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.grey200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.grey200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.grey300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.grey300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.grey400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.grey400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.grey500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.grey500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.grey600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.grey600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.grey700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.grey800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.grey800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.grey900));
        SetExtraClicks();

        RunIVisibleHelper();
    }

    public void BlueGreyClick(){
        caption.setText("BlueGrey");
        SetWhiteness();
        hex50.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey50), PorterDuff.Mode.SRC_ATOP);
        hex50.setText(c0+sp+getText(R.color.blue_grey50));

        hex100.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey100), PorterDuff.Mode.SRC_ATOP);
        hex100.setText(c1+sp+getText(R.color.blue_grey100));

        hex200.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey200), PorterDuff.Mode.SRC_ATOP);
        hex200.setText(c2+sp+getText(R.color.blue_grey200));

        hex300.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey300), PorterDuff.Mode.SRC_ATOP);
        hex300.setText(c3+sp+getText(R.color.blue_grey300));

        hex400.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey400), PorterDuff.Mode.SRC_ATOP);
        hex400.setText(c4+sp+getText(R.color.blue_grey400));

        hex500.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey500), PorterDuff.Mode.SRC_ATOP);
        hex500.setText(c5+sp+getText(R.color.blue_grey500));

        hex600.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey600), PorterDuff.Mode.SRC_ATOP);
        hex600.setText(c6+sp+getText(R.color.blue_grey600));

        hex700.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey700), PorterDuff.Mode.SRC_ATOP);
        hex700.setText(c7+sp+getText(R.color.blue_grey700));

        hex800.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey800), PorterDuff.Mode.SRC_ATOP);
        hex800.setText(c8+sp+getText(R.color.blue_grey800));

        hex900.getBackground().setColorFilter(getResources().getColor(R.color.blue_grey900), PorterDuff.Mode.SRC_ATOP);
        hex900.setText(c9+sp+getText(R.color.blue_grey900));

        SetExtraClicks();
        RunIVisibleHelper();
    }

}
