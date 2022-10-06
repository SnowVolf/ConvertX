package ru.svolf.convertx.palette;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.svolf.convertx.R;
import ru.svolf.convertx.ui.Toasty;
import ru.svolf.convertx.ui.activity.BaseActivity;
import ru.svolf.convertx.utils.StringUtils;

@SuppressWarnings({"ResourceAsColor", "ResourceType", "deprecation", "SetTextI18n"})
public class PaletteActivity extends BaseActivity {
    private Button red, pink, purple, deep_purple, indigo, blue, light_blue, cyan, teal,
           green, light_green, lime, yellow, amber, orange, deep_orange, brown, grey, blue_grey,
    //buttons colors
     hex50, hex100, hex200, hex300, hex400, hex500, hex600, hex700, hex800, hex900,
            hexa100, hexa200, hexa400, hexa700;
    private TextView caption, code;
    private String[] colorNames = {"50", "100", "200", "300", "400", "500", "600", "700", "800", "900", "a100", "a200", "a400", "a700"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette);
        InitMainViews();
        InitExtraViews();
        SetClicks();
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
        /*
          Java8 - сахар
         */
        red.setOnClickListener(__ -> RedClick());
        pink.setOnClickListener(__ -> PinkClick());
        purple.setOnClickListener(__ -> PurpleClick());
        deep_purple.setOnClickListener(__ -> DeepPurpleClick());
        indigo.setOnClickListener(__ -> IndigoClick());
        blue.setOnClickListener(__ -> BlueClick());
        light_blue.setOnClickListener(__ -> LightBlueClick());
        cyan.setOnClickListener(__ -> CyanClick());
        teal.setOnClickListener(__ -> TealClick());
        green.setOnClickListener(__ -> GreenClick());
        light_green.setOnClickListener(__ -> LightGreenClick());
        lime.setOnClickListener(__ -> LimeClick());
        yellow.setOnClickListener(__ -> YellowClick());
        amber.setOnClickListener(__ -> AmberClick());
        orange.setOnClickListener(__ -> OrangeClick());
        deep_orange.setOnClickListener(__ -> DeepOrangeClick());
        brown.setOnClickListener(__ -> BrownClick());
        grey.setOnClickListener(__ -> GreyClick());
        blue_grey.setOnClickListener(__ -> BlueGreyClick());
    }

    public void SetExtraClicks(){
            hex50.setOnClickListener(__ -> {
                code.setText(hex50.getText().toString().substring(12));
                showToastNotification();
                StringUtils.INSTANCE.copyToClipboard(this, hex50.getText().toString().substring(12));
            });
        hex100.setOnClickListener(__ -> {
            code.setText(hex100.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex100.getText().toString().substring(13));
        });
        hex200.setOnClickListener(__ -> {
            code.setText(hex200.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex200.getText().toString().substring(13));
        });
        hex300.setOnClickListener(__ -> {
            code.setText(hex300.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex300.getText().toString().substring(13));
        });
        hex400.setOnClickListener(__ -> {
            code.setText(hex400.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex400.getText().toString().substring(13));
        });
        hex500.setOnClickListener(__ -> {
            code.setText(hex500.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex500.getText().toString().substring(13));
        });
        hex600.setOnClickListener(__ -> {
            code.setText(hex600.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex600.getText().toString().substring(13));
        });
        hex700.setOnClickListener(__ -> {
            code.setText(hex700.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex700.getText().toString().substring(13));
        });
        hex800.setOnClickListener(__ -> {
            code.setText(hex800.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex800.getText().toString().substring(13));
        });
        hex900.setOnClickListener(__ -> {
            code.setText(hex900.getText().toString().substring(13));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hex900.getText().toString().substring(13));
        });
        hexa100.setOnClickListener(__ -> {
            code.setText(hexa100.getText().toString().substring(14));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hexa100.getText().toString().substring(14));
        });
        hexa200.setOnClickListener(__ -> {
            code.setText(hexa200.getText().toString().substring(14));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hexa200.getText().toString().substring(14));
        });
        hexa400.setOnClickListener(__ -> {
            code.setText(hexa400.getText().toString().substring(14));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hexa400.getText().toString().substring(14));
        });
        hexa700.setOnClickListener(__ -> {
            code.setText(hexa700.getText().toString().substring(14));
            showToastNotification();
            StringUtils.INSTANCE.copyToClipboard(this, hexa700.getText().toString().substring(14));
        });
    }

    public void SetDarkness(){
        //делаем черный тескст на слишком светлых кнопках, чтоб можно было различить текст
        //на самом деле он не черный, а какой-то серый. Хз почему так.
        hex400.setTextColor(Color.BLACK);
        hex500.setTextColor(Color.BLACK);
        hex600.setTextColor(Color.BLACK);
        hex700.setTextColor(Color.BLACK);
        hexa400.setTextColor(Color.BLACK);
        
    }

    public void SetWhiteness(){
        //и так понятно
        hex400.setTextColor(Color.WHITE);
        hex500.setTextColor(Color.WHITE);
        hex600.setTextColor(Color.WHITE);
        hex700.setTextColor(Color.WHITE);
        hexa400.setTextColor(Color.WHITE);
        
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
        setGirl(hex50, R.color.red50, 0);
        setGirl(hex100, R.color.red100, 1);
        setGirl(hex200, R.color.red200, 2);
        setGirl(hex300, R.color.red300, 3);
        setGirl(hex400, R.color.red400, 4);
        setGirl(hex500, R.color.red500, 5);
        setGirl(hex600, R.color.red600, 6);
        setGirl(hex700, R.color.red700, 7);
        setGirl(hex800, R.color.red800, 8);
        setGirl(hex900, R.color.red900, 9);
        setGirl(hexa100, R.color.reda100, 10);
        setGirl(hexa200, R.color.reda200, 11);
        setGirl(hexa400, R.color.reda400, 12);
        setGirl(hexa700, R.color.reda700, 13);
        SetExtraClicks();
    }

    public void PinkClick(){
        caption.setText("Pink");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.pink50, 0);
        setGirl(hex100, R.color.pink100, 1);
        setGirl(hex200, R.color.pink200, 2);
        setGirl(hex300, R.color.pink300, 3);
        setGirl(hex400, R.color.pink400, 4);
        setGirl(hex500, R.color.pink500, 5);
        setGirl(hex600, R.color.pink600, 6);
        setGirl(hex700, R.color.pink700, 7);
        setGirl(hex800, R.color.pink800, 8);
        setGirl(hex900, R.color.pink900, 9);
        setGirl(hexa100, R.color.pinka100, 10);
        setGirl(hexa200, R.color.pinka200, 11);
        setGirl(hexa400, R.color.pinka400, 12);
        setGirl(hexa700, R.color.pinka700, 13);
        SetExtraClicks();
    }

    public void PurpleClick(){
        caption.setText("Purple");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.purple50, 0);
        setGirl(hex100, R.color.purple100, 1);
        setGirl(hex200, R.color.purple200, 2);
        setGirl(hex300, R.color.purple300, 3);
        setGirl(hex400, R.color.purple400, 4);
        setGirl(hex500, R.color.purple500, 5);
        setGirl(hex600, R.color.purple600, 6);
        setGirl(hex700, R.color.purple700, 7);
        setGirl(hex800, R.color.purple800, 8);
        setGirl(hex900, R.color.purple900, 9);
        setGirl(hexa100, R.color.purplea100, 10);
        setGirl(hexa200, R.color.purplea200, 11);
        setGirl(hexa400, R.color.purplea400, 12);
        setGirl(hexa700, R.color.purplea700, 13);
        SetExtraClicks();
    }

    public void DeepPurpleClick(){
        caption.setText("Deep Purple");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.deep_purple50, 0);
        setGirl(hex100, R.color.deep_purple100, 1);
        setGirl(hex200, R.color.deep_purple200, 2);
        setGirl(hex300, R.color.deep_purple300, 3);
        setGirl(hex400, R.color.deep_purple400, 4);
        setGirl(hex500, R.color.deep_purple500, 5);
        setGirl(hex600, R.color.deep_purple600, 6);
        setGirl(hex700, R.color.deep_purple700, 7);
        setGirl(hex800, R.color.deep_purple800, 8);
        setGirl(hex900, R.color.deep_purple900, 9);
        setGirl(hexa100, R.color.deep_purplea100, 10);
        setGirl(hexa200, R.color.deep_purplea200, 11);
        setGirl(hexa400, R.color.deep_purplea400, 12);
        setGirl(hexa700, R.color.deep_purplea700, 13);
        SetExtraClicks();
    }

    public void IndigoClick(){
        caption.setText("Indigo");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.indigo50, 0);
        setGirl(hex100, R.color.indigo100, 1);
        setGirl(hex200, R.color.indigo200, 2);
        setGirl(hex300, R.color.indigo300, 3);
        setGirl(hex400, R.color.indigo400, 4);
        setGirl(hex500, R.color.indigo500, 5);
        setGirl(hex600, R.color.indigo600, 6);
        setGirl(hex700, R.color.indigo700, 7);
        setGirl(hex800, R.color.indigo800, 8);
        setGirl(hex900, R.color.indigo900, 9);
        setGirl(hexa100, R.color.indigoa100, 10);
        setGirl(hexa200, R.color.indigoa200, 11);
        setGirl(hexa400, R.color.indigoa400, 12);
        setGirl(hexa700, R.color.indigoa700, 13);
        SetExtraClicks();
    }

    public void BlueClick(){
        caption.setText("Blue");
        SetWhiteness();
        RunVisibleHelper();
        setGirl(hex50, R.color.blue50, 0);
        setGirl(hex100, R.color.blue100, 1);
        setGirl(hex200, R.color.blue200, 2);
        setGirl(hex300, R.color.blue300, 3);
        setGirl(hex400, R.color.blue400, 4);
        setGirl(hex500, R.color.blue500, 5);
        setGirl(hex600, R.color.blue600, 6);
        setGirl(hex700, R.color.blue700, 7);
        setGirl(hex800, R.color.blue800, 8);
        setGirl(hex900, R.color.blue900, 9);
        setGirl(hexa100, R.color.bluea100, 10);
        setGirl(hexa200, R.color.bluea200, 11);
        setGirl(hexa400, R.color.bluea400, 12);
        setGirl(hexa700, R.color.bluea700, 13);
        SetExtraClicks();
    }

    public void LightBlueClick(){
        caption.setText("Light-Blue");
        SetWhiteness();
        RunVisibleHelper();
        setGirl(hex50, R.color.light_blue50, 0);
        setGirl(hex100, R.color.light_blue100, 1);
        setGirl(hex200, R.color.light_blue200, 2);
        setGirl(hex300, R.color.light_blue300, 3);
        setGirl(hex400, R.color.light_blue400, 4);
        setGirl(hex500, R.color.light_blue500, 5);
        setGirl(hex600, R.color.light_blue600, 6);
        setGirl(hex700, R.color.light_blue700, 7);
        setGirl(hex800, R.color.light_blue800, 8);
        setGirl(hex900, R.color.light_blue900, 9);
        setGirl(hexa100, R.color.light_bluea100, 10);
        setGirl(hexa200, R.color.light_bluea200, 11);
        setGirl(hexa400, R.color.light_bluea400, 12);
        setGirl(hexa700, R.color.light_bluea700, 13);
        SetExtraClicks();
    }

    public void CyanClick(){
        caption.setText("Cyan");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.cyan50, 0);
        setGirl(hex100, R.color.cyan100, 1);
        setGirl(hex200, R.color.cyan200, 2);
        setGirl(hex300, R.color.cyan300, 3);
        setGirl(hex400, R.color.cyan400, 4);
        setGirl(hex500, R.color.cyan500, 5);
        setGirl(hex600, R.color.cyan600, 6);
        setGirl(hex700, R.color.cyan700, 7);
        setGirl(hex800, R.color.cyan800, 8);
        setGirl(hex900, R.color.cyan900, 9);
        setGirl(hexa100, R.color.cyana100, 10);
        setGirl(hexa200, R.color.cyana200, 11);
        setGirl(hexa400, R.color.cyana400, 12);
        setGirl(hexa700, R.color.cyana700, 13);
        SetExtraClicks();
    }

    public void TealClick(){
        caption.setText("Teal");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.teal50, 0);
        setGirl(hex100, R.color.teal100, 1);
        setGirl(hex200, R.color.teal200, 2);
        setGirl(hex300, R.color.teal300, 3);
        setGirl(hex400, R.color.teal400, 4);
        setGirl(hex500, R.color.teal500, 5);
        setGirl(hex600, R.color.teal600, 6);
        setGirl(hex700, R.color.teal700, 7);
        setGirl(hex800, R.color.teal800, 8);
        setGirl(hex900, R.color.teal900, 9);
        setGirl(hexa100, R.color.teala100, 10);
        setGirl(hexa200, R.color.teala200, 11);
        setGirl(hexa400, R.color.teala400, 12);
        setGirl(hexa700, R.color.teala700, 13);
        SetExtraClicks();
    }

    public void GreenClick(){
        caption.setText("Green");
        RunVisibleHelper();
        SetWhiteness();
        setGirl(hex50, R.color.green50, 0);
        setGirl(hex100, R.color.green100, 1);
        setGirl(hex200, R.color.green200, 2);
        setGirl(hex300, R.color.green300, 3);
        setGirl(hex400, R.color.green400, 4);
        setGirl(hex500, R.color.green500, 5);
        setGirl(hex600, R.color.green600, 6);
        setGirl(hex700, R.color.green700, 7);
        setGirl(hex800, R.color.green800, 8);
        setGirl(hex900, R.color.green900, 9);
        setGirl(hexa100, R.color.greena100, 10);
        setGirl(hexa200, R.color.greena200, 11);
        setGirl(hexa400, R.color.greena400, 12);
        setGirl(hexa700, R.color.greena700, 13);
        SetExtraClicks();
    }

    public void LightGreenClick(){
        RunVisibleHelper();
        SetWhiteness();
        caption.setText("Light-Green");
        setGirl(hex50, R.color.light_green50, 0);
        setGirl(hex100, R.color.light_green100, 1);
        setGirl(hex200, R.color.light_green200, 2);
        setGirl(hex300, R.color.light_green300, 3);
        setGirl(hex400, R.color.light_green400, 4);
        setGirl(hex500, R.color.light_green500, 5);
        setGirl(hex600, R.color.light_green600, 6);
        setGirl(hex700, R.color.light_green700, 7);
        setGirl(hex800, R.color.light_green800, 8);
        setGirl(hex900, R.color.light_green900, 9);
        setGirl(hexa100, R.color.light_greena100, 10);
        setGirl(hexa200, R.color.light_greena200, 11);
        setGirl(hexa400, R.color.light_greena400, 12);
        setGirl(hexa700, R.color.light_greena700, 13);
        SetExtraClicks();
    }

    public void LimeClick(){
        caption.setText("Lime");
        RunVisibleHelper();
        SetDarkness();
        setGirl(hex50, R.color.lime50, 0);
        setGirl(hex100, R.color.lime100, 1);
        setGirl(hex200, R.color.lime200, 2);
        setGirl(hex300, R.color.lime300, 3);
        setGirl(hex400, R.color.lime400, 4);
        setGirl(hex500, R.color.lime500, 5);
        setGirl(hex600, R.color.lime600, 6);
        setGirl(hex700, R.color.lime700, 7);
        setGirl(hex800, R.color.lime800, 8);
        setGirl(hex900, R.color.lime900, 9);
        setGirl(hexa100, R.color.limea100, 10);
        setGirl(hexa200, R.color.limea200, 11);
        setGirl(hexa400, R.color.limea400, 12);
        setGirl(hexa700, R.color.limea700, 13);
        SetExtraClicks();
    }

    public void YellowClick(){
        caption.setText("Yellow");
        RunVisibleHelper();
        SetDarkness();
        setGirl(hex50, R.color.yellow50, 0);
        setGirl(hex100, R.color.yellow100, 1);
        setGirl(hex200, R.color.yellow200, 2);
        setGirl(hex300, R.color.yellow300, 3);
        setGirl(hex400, R.color.yellow400, 4);
        setGirl(hex500, R.color.yellow500, 5);
        setGirl(hex600, R.color.yellow600, 6);
        setGirl(hex700, R.color.yellow700, 7);
        setGirl(hex800, R.color.yellow800, 8);
        setGirl(hex900, R.color.yellow900, 9);
        setGirl(hexa100, R.color.yellowa100, 10);
        setGirl(hexa200, R.color.yellowa200, 11);
        setGirl(hexa400, R.color.yellowa400, 12);
        setGirl(hexa700, R.color.yellowa700, 13);
        SetExtraClicks();
    }

    public void AmberClick(){
        caption.setText("Amber");
        RunVisibleHelper();
        SetDarkness();
        setGirl(hex50, R.color.amber50, 0);
        setGirl(hex100, R.color.amber100, 1);
        setGirl(hex200, R.color.amber200, 2);
        setGirl(hex300, R.color.amber300, 3);
        setGirl(hex400, R.color.amber400, 4);
        setGirl(hex500, R.color.amber500, 5);
        setGirl(hex600, R.color.amber600, 6);
        setGirl(hex700, R.color.amber700, 7);
        setGirl(hex800, R.color.amber800, 8);
        setGirl(hex900, R.color.amber900, 9);
        setGirl(hexa100, R.color.ambera100, 10);
        setGirl(hexa200, R.color.ambera200, 11);
        setGirl(hexa400, R.color.ambera400, 12);
        setGirl(hexa700, R.color.ambera700, 13);
        SetExtraClicks();
    }

    public void OrangeClick(){
        caption.setText("Orange");
        RunVisibleHelper();
        SetDarkness();
        setGirl(hex50, R.color.orange50, 0);
        setGirl(hex100, R.color.orange100, 1);
        setGirl(hex200, R.color.orange200, 2);
        setGirl(hex300, R.color.orange300, 3);
        setGirl(hex400, R.color.orange400, 4);
        setGirl(hex500, R.color.orange500, 5);
        setGirl(hex600, R.color.orange600, 6);
        setGirl(hex700, R.color.orange700, 7);
        setGirl(hex800, R.color.orange800, 8);
        setGirl(hex900, R.color.orange900, 9);
        setGirl(hexa100, R.color.orangea100, 10);
        setGirl(hexa200, R.color.orangea200, 11);
        setGirl(hexa400, R.color.orangea400, 12);
        setGirl(hexa700, R.color.orangea700, 13);
        SetExtraClicks();
    }

    public void DeepOrangeClick(){
        caption.setText("Deep Orange");
        RunVisibleHelper();
        SetDarkness();
        setGirl(hex50, R.color.deep_orange50, 0);
        setGirl(hex100, R.color.deep_orange100, 1);
        setGirl(hex200, R.color.deep_orange200, 2);
        setGirl(hex300, R.color.deep_orange300, 3);
        setGirl(hex400, R.color.deep_orange400, 4);
        setGirl(hex500, R.color.deep_orange500, 5);
        setGirl(hex600, R.color.deep_orange600, 6);
        setGirl(hex700, R.color.deep_orange700, 7);
        setGirl(hex800, R.color.deep_orange800, 8);
        setGirl(hex900, R.color.deep_orange900, 9);
        setGirl(hexa100, R.color.deep_orangea100, 10);
        setGirl(hexa200, R.color.deep_orangea200, 11);
        setGirl(hexa400, R.color.deep_orangea400, 12);
        setGirl(hexa700, R.color.deep_orangea700, 13);
        SetExtraClicks();
    }

    public void BrownClick(){
        caption.setText("Brown");
        SetWhiteness();
        setGirl(hex50, R.color.brown50, 0);
        setGirl(hex100, R.color.brown100, 1);
        setGirl(hex200, R.color.brown200, 2);
        setGirl(hex300, R.color.brown300, 3);
        setGirl(hex400, R.color.brown400, 4);
        setGirl(hex500, R.color.brown500, 5);
        setGirl(hex600, R.color.brown600, 6);
        setGirl(hex700, R.color.brown700, 7);
        setGirl(hex800, R.color.brown800, 8);
        setGirl(hex900, R.color.brown900, 9);
        SetExtraClicks();

       RunIVisibleHelper();
    }

    public void GreyClick(){
        caption.setText("Grey");
        SetWhiteness();
        setGirl(hex50, R.color.grey50, 0);
        setGirl(hex100, R.color.grey100, 1);
        setGirl(hex200, R.color.grey200, 2);
        setGirl(hex300, R.color.grey300, 3);
        setGirl(hex400, R.color.grey400, 4);
        setGirl(hex500, R.color.grey500, 5);
        setGirl(hex600, R.color.grey600, 6);
        setGirl(hex700, R.color.grey700, 7);
        setGirl(hex800, R.color.grey800, 8);
        setGirl(hex900, R.color.grey900, 9);
        SetExtraClicks();

        RunIVisibleHelper();
    }

    public void BlueGreyClick(){
        caption.setText("BlueGrey");
        SetWhiteness();
        setGirl(hex50, R.color.blue_grey50, 0);
        setGirl(hex100, R.color.blue_grey100, 1);
        setGirl(hex200, R.color.blue_grey200, 2);
        setGirl(hex300, R.color.blue_grey300, 3);
        setGirl(hex400, R.color.blue_grey400, 4);
        setGirl(hex500, R.color.blue_grey500, 5);
        setGirl(hex600, R.color.blue_grey600, 6);
        setGirl(hex700, R.color.blue_grey700, 7);
        setGirl(hex800, R.color.blue_grey800, 8);
        setGirl(hex900, R.color.blue_grey900, 9);
        SetExtraClicks();
        RunIVisibleHelper();
    }

    private void showToastNotification(){
        Toasty.info(this, R.string.copied2clipboard, Toast.LENGTH_SHORT, true).show();
    }

    private void setGirl(Button button, int color, int arrayPosition){
        button.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        String space = "          ";
        button.setText(colorNames[arrayPosition]+ space + getText(color));
    }
}