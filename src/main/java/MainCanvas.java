import java.awt.*;
import java.applet.*;

class MainCanvas extends Canvas
{
    private String _strText = null;

    private String _strFace = "Dialog";
    private String _strStyle = "Plain";
    private String _strSize = "24";

    public MainCanvas(String strText)
    {
        _strText = strText;
    }

    public void notify(String strFace, String strStyle, String strSize)
    {
        _strFace = strFace;
        _strStyle = strStyle;
        _strSize = strSize;

        repaint();
    }

    public void paint(Graphics g)
    {
        // we must manually blank out the background ourselves in order
        // to avoid bugs in some implementations of the AWT...

        g.setColor(Color.white);

        g.fillRect(0, 0, size().width, size().height);

        // determine the parameters of the new font

        int nStyle = Font.PLAIN;

        if (_strStyle.equals("Bold")) nStyle = Font.BOLD;
        if (_strStyle.equals("Italic")) nStyle = Font.ITALIC;

        int nSize = 24;

        try
        {
            nSize = Integer.parseInt(_strSize);
        }
        catch (NumberFormatException e)
        {
            nSize = 24;
        }

        // create and set the new font

        Font f = new Font(_strFace, nStyle, nSize);

        g.setFont(f);

        // get its metrics

        FontMetrics fm1 = g.getFontMetrics();

        int a1 = fm1.getAscent();

        int l1 = fm1.getLeading();

        int d1 = fm1.getDescent();

        int w1 = fm1.stringWidth(_strText);

        int x1 = (size().width - w1) / 2;

        // draw baseline, ascent, and descent

        g.setColor(Color.black);

        g.drawLine(x1 - 10, 100, x1 + 10 + w1, 100);
        g.drawLine(x1 - 10, 101, x1 + 10 + w1, 101);

        g.drawLine(x1 - 10, 100 - a1, x1 + 10 + w1, 100 - a1);

        g.drawLine(x1 - 10, 100 + d1, x1 + 10 + w1, 100 + d1);

        // draw "ascent" and "descent"

        g.setFont(new Font("Dialog", Font.PLAIN, 12));

        FontMetrics fm2 = g.getFontMetrics();

        int w2 = fm2.stringWidth("ascent");

        g.drawString("ascent", x1 + 10 + w1 - w2, 100 - a1 - 2);

        w2 = fm2.stringWidth("descent");

        int a2 = fm2.getAscent();

        g.drawString("descent", x1 + 10 + w1 - w2, 100 + d1 + a2 + 2);

        // draw the width lines

        int x = x1;

        for (int i = 0; i < _strText.length() && x < x1 + 10 + w1 - w2; i++)
        {
            g.drawLine(x, 105, x, 95 - a1);

            x += fm1.charWidth(_strText.charAt(i));
        }

        // draw the text

        g.setFont(f);

        g.setColor(Color.blue);

        g.drawString(_strText, x1, 100);
    }
}

class Main extends Applet
{
    private MainCanvas can = null;

    private Choice chFace = new Choice();
    private Choice chStyle = new Choice();
    private Choice chSize = new Choice();

    public void init()
    {
        char [] rgc = { 'A', 'a', 'F', 'f', 'G', 'g', 115, 125, 200, 221, 189 ,'і', 'ї'};

        can = new MainCanvas(new String(rgc));

        setLayout(new BorderLayout());

        setBackground(Color.white);

        // create the font menu

        String [] rgstr = getToolkit().getFontList();

        for (int i = 0; i < rgstr.length; i++) chFace.addItem(rgstr[i]);

        // create the style menu

        chStyle.addItem("Plain");
        chStyle.addItem("Italic");
        chStyle.addItem("Bold");

        // create the size menu

        for (int j = 12; j < 30; j++) chSize.addItem(String.valueOf(j * 2));

        chSize.select("36");

        Panel p = new Panel();

        p.add(chFace);
        p.add(chStyle);
        p.add(chSize);

        add("North", p);

        add("Center", can);

        can.notify(chFace.getSelectedItem(), chStyle.getSelectedItem(),
                chSize.getSelectedItem());
    }

    public Dimension preferredSize()
    {
        return new Dimension(500, 300);
    }

    public boolean action(Event e, Object o)
    {
        if (e.target == chFace || e.target == chStyle || e.target == chSize)
        {
            can.notify(chFace.getSelectedItem(), chStyle.getSelectedItem(),
                    chSize.getSelectedItem());

            return true;
        }

        return false;
    }

    public static void main(String [] args)
    {
        Frame f = new Frame("Main");

        Main m = new Main();

        m.init();

        f.add("Center", m);

        f.pack();
        f.show();
    }
}
