package view.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import mytools.MyUtil;

public class MyIconFactory {

    private static Icon CheckBoxIcon;
    private static Icon RadioButtonIcon;

    private static Icon OptionPaneErrorIcon;
    private static Icon OptionPaneInformationIcon;
    private static Icon OptionPaneQuestionIcon;
    private static Icon OptionPaneWarningIcon;

    private static Icon FileChooserHomeFolderIcon;
    private static Icon FileChooserUpFolderIcon;
    private static Icon FileChooserNewFolderIcon;
    private static Icon FileChooserDetailsViewIcon;
    private static Icon FileChooserListViewIcon;

    private static Icon ReFreshIcon;

    private static Icon FoldIcon, VioceWarn, RollScreen, ShowDebug, MsgWarn;

    private static Icon RainLogo, WindLogo, WetLogo, TemperatureLogo,
            WaterLogo, PressureLogo, FlowLogo;

    public static Icon getFlowLogo() {
        if (FlowLogo == null) {
            FlowLogo = new ImageIcon(
                    MyIconFactory.class.getResource("flow_24.png"));
        }
        return FlowLogo;
    }

    public static Icon getRainLogo() {
        if (RainLogo == null) {
            RainLogo = new ImageIcon(
                    MyIconFactory.class.getResource("rain_24.png"));
        }
        return RainLogo;
    }

    public static Icon getWindLogo() {
        if (WindLogo == null) {
            WindLogo = new ImageIcon(
                    MyIconFactory.class.getResource("wind_24.png"));
        }
        return WindLogo;
    }

    public static Icon getWetLogo() {
        if (WetLogo == null) {
            WetLogo = new ImageIcon(
                    MyIconFactory.class.getResource("wet_24.png"));
        }
        return WetLogo;
    }

    public static Icon getTemperatureLogo() {
        if (TemperatureLogo == null) {
            TemperatureLogo = new ImageIcon(
                    MyIconFactory.class.getResource("temperature_24.png"));
        }
        return TemperatureLogo;
    }

    public static Icon getWaterLogo() {
        if (WaterLogo == null) {
            WaterLogo = new ImageIcon(
                    MyIconFactory.class.getResource("water_24.png"));
        }
        return WaterLogo;
    }

    public static Icon getPressureLogo() {
        if (PressureLogo == null) {
            PressureLogo = new ImageIcon(
                    MyIconFactory.class.getResource("pressure_24.png"));
        }
        return PressureLogo;
    }

    public static Icon getFoldIcon() {
        if (FoldIcon == null) {
            FoldIcon = new FoldIcon();
        }
        return FoldIcon;
    }

    public static Icon getCheckBoxIcon() {
        if (CheckBoxIcon == null) {
            CheckBoxIcon = new MyCheckBoxIcon();
        }
        return CheckBoxIcon;
    }

    public static Icon getRadioButtonIcon() {
        if (RadioButtonIcon == null) {
            RadioButtonIcon = new MyRadioButtonIcon();
        }
        return RadioButtonIcon;
    }

    public static Icon getOptionPaneErrorIcon() {
        if (OptionPaneErrorIcon == null) {
            OptionPaneErrorIcon = new ImageIcon(
                    MyIconFactory.class.getResource("error.png"));
        }
        return OptionPaneErrorIcon;
    }

    public static Icon getOptionPaneQuestionIcon() {
        if (OptionPaneQuestionIcon == null) {
            OptionPaneQuestionIcon = new ImageIcon(
                    MyIconFactory.class.getResource("question.png"));
        }
        return OptionPaneQuestionIcon;
    }

    public static Icon getOptionPaneInformationIcon() {
        if (OptionPaneInformationIcon == null) {
            OptionPaneInformationIcon = new ImageIcon(
                    MyIconFactory.class.getResource("info.png"));
        }
        return OptionPaneInformationIcon;
    }

    public static Icon getOptionPaneWarningIcon() {
        if (OptionPaneWarningIcon == null) {
            OptionPaneWarningIcon = new ImageIcon(
                    MyIconFactory.class.getResource("warning.png"));
        }
        return OptionPaneWarningIcon;
    }

    public static Icon getReFreshIcon() {
        if (ReFreshIcon == null) {
            ReFreshIcon = new ImageIcon("images/refresh.png");
        }
        return ReFreshIcon;
    }

    public static Icon getVoiceWarnIcon() {
        if (VioceWarn == null) {
            VioceWarn = new VioceWarn_16();
        }
        return VioceWarn;
    }

    public static Icon getMsgWarnIcon() {
        if (MsgWarn == null) {
            MsgWarn = new MsgWarn_16();
        }
        return MsgWarn;
    }

    public static Icon getRollScreenIcon() {
        if (RollScreen == null) {
            RollScreen = new RollScreen_16();
        }
        return RollScreen;
    }

    public static Icon getShowDebugIcon() {
        if (ShowDebug == null) {
            ShowDebug = new ShowDebug_16();
        }
        return ShowDebug;
    }

    static Image sound_16, sound_muted_16, rollscreen_16, rollscreen_muted_16, showDebug_16, showDebug_muted_16, msg_16, msg_muted_16,
            temp, vari, SF6, ladder, unknown, SF6_28, temp_28, vari_28;

    static {
        try {
            sound_16 = ImageIO.read(MyIconFactory.class.getResource("sound_16.png"));
            sound_muted_16 = ImageIO.read(MyIconFactory.class.getResource("sound_muted_16.png"));
            rollscreen_16 = ImageIO.read(MyIconFactory.class.getResource("rollscreen_16.png"));
            rollscreen_muted_16 = ImageIO.read(MyIconFactory.class.getResource("rollscreen_muted_16.png"));
            showDebug_16 = ImageIO.read(MyIconFactory.class.getResource("showDebug_16.png"));
            showDebug_muted_16 = ImageIO.read(MyIconFactory.class.getResource("showDebug_muted_16.png"));
            msg_16 = ImageIO.read(MyIconFactory.class.getResource("msg_16.png"));
            msg_muted_16 = ImageIO.read(MyIconFactory.class.getResource("msg_muted_16.png"));
            temp = ImageIO.read(MyIconFactory.class.getResource("temp.png"));
            temp_28 = ImageIO.read(MyIconFactory.class.getResource("temp_28.png"));
            vari = ImageIO.read(MyIconFactory.class.getResource("vari.png"));
            vari_28 = ImageIO.read(MyIconFactory.class.getResource("vari_28.png"));
            SF6 = ImageIO.read(MyIconFactory.class.getResource("sf6.png"));
            SF6_28 = ImageIO.read(MyIconFactory.class.getResource("sf6_28.png"));
            ladder = ImageIO.read(MyIconFactory.class.getResource("ladder.png"));
            unknown = ImageIO.read(MyIconFactory.class.getResource("unknown.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
