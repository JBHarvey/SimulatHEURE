/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Jean-Benoît
 */
public class Text extends Displayable {

    private String title;
    private String content;
    private String backup;
    private String separator;
    private String units;
    private Font textFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private Color titleColor = Color.WHITE;
    private Color contentColor = Color.WHITE;
    private Color cursorColor;
    private Color contentBoxColor;
    private Viewport unitBox;
    private Viewport contentBox;
    private Viewport titleBox;
    private Viewport cursor = new Viewport(0, 0, 10, 20);
    private boolean textChanged = false;
    private int cursorPosition;
    private int cursorStep = 10;
    private final int cursorExtension = 5;
    public static final String TABULATION = "    ";

    public Text(String name) {
        super(name);
        contentBoxColor = INVISIBLE;
        cursorColor = INVISIBLE;
        content = " ";
        separator = " : ";
        units = "";
        title = name;
        editable = true;
    }

    @Override
    public void paint(Graphics2D graphics2D) {

        saveGraphicsConfigurations(graphics2D);

        updateTextBounds(graphics2D);

//          For debug only, can be deleted once development is done
//        paintBackground(graphics2D, contentBox, Color.RED);
        paintTitle(graphics2D);
        paintContent(graphics2D);
        paintCursor(graphics2D);
        paintUnits(graphics2D);

        restoreGraphicsConfigurations(graphics2D);
    }

    protected void paintBackground(Graphics2D graphics2D, Viewport textBackground, Color colour) {
        graphics2D.setColor(colour);
        graphics2D.fill(textBackground.getShape());
    }

    protected void paintTitle(Graphics2D graphics2D) {
        graphics2D.setFont(textFont);
        graphics2D.setColor(titleColor);
        graphics2D.drawString(titleAndSeparator(), (int) relativeX, (int) relativeY);
    }

    protected void paintContent(Graphics2D graphics2D) {
        graphics2D.setFont(textFont);
        graphics2D.setColor(contentBoxColor);
        graphics2D.fill(contentBox.getShape());
        graphics2D.setColor(contentColor);
        graphics2D.drawString(content, (int) contentBox.getX(), (int) relativeY);

    }

    public void paintCursor(Graphics2D graphics2D) {
        if (editable) {
            graphics2D.setColor(cursorColor);
            graphics2D.fill(cursor.getShape());
        }
    }

    private void paintUnits(Graphics2D graphics2D) {
        graphics2D.setFont(textFont);
        graphics2D.setColor(titleColor);
        graphics2D.drawString(units, (int) unitBox.getX(), (int) relativeY);
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        relativeX = viewport.getX();
        relativeY = viewport.getY();
    }

    public void updateTextBounds(Graphics2D graphics2D) {
        if (textChanged) {
            titleContentUnitsBounds(graphics2D);
            setTextShape();
            processCursorAttributes(graphics2D);
        }
    }

    private void titleContentUnitsBounds(Graphics2D graphics2D) {
        Viewport textBounds = new Viewport(textFont.getStringBounds(titleAndSeparator(), graphics2D.getFontRenderContext()));
        titleBox = new Viewport(relativeX, relativeY - 15, textBounds.getWidth(), textBounds.getHeight() + 2);

        textBounds = new Viewport(textFont.getStringBounds(content, graphics2D.getFontRenderContext()));
        contentBox = new Viewport(titleBox.getX() + titleBox.getWidth(), relativeY - 15, textBounds.getWidth() + 2, textBounds.getHeight() + 2);

        textBounds = new Viewport(textFont.getStringBounds(units, graphics2D.getFontRenderContext()));
        unitBox = new Viewport(contentBox.getX() + contentBox.getWidth() + 5, relativeY - 15, textBounds.getWidth() + 2, textBounds.getHeight() + 2);
    }

    private void setTextShape() {
        double width = contentBox.getWidth() + titleBox.getWidth() + cursorExtension;
        elementShape = new Viewport(relativeX, relativeY - 16, width, titleBox.getHeight() + 4);
        textChanged = false;
    }

    private void processCursorAttributes(Graphics2D graphics2D) {
        Viewport charBounds = new Viewport(textFont.getStringBounds("A", graphics2D.getFontRenderContext()));
        cursorStep = (int) charBounds.getWidth();
        cursor = new Viewport((cursorPosition * cursorStep) + contentBox.getX(), relativeY - 14, 3, charBounds.getHeight());
    }

    /**
     * Moves the text back horizontally on half its width. Use this one with
     * caution. It can only be called once setRenderProperties and
     * updateTextbounds has been, else it will crash.
     */
    public void centerHorizontally() {
        textChanged = true;
        relativeX -= contentBox.getWidth() / 2;
    }

    public void setContent(String content) {
        this.content = content;
        textChanged = true;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setFont(Font font) {
        textFont = font;
    }

    public void setFontSize(int size) {
        textFont = new Font(Font.MONOSPACED, Font.BOLD, size);
    }

    @Override
    public String toString() {
        return title + separator + content + units;
    }

    protected String titleAndSeparator() {
        return title + separator;
    }

    public String getContent() {
        return content;
    }

    public String getUnits() {
        return units;
    }

    public void setSeparator(String newSeparator) {
        separator = newSeparator;
    }

    public void setContentColor(Color contentColor) {
        this.contentColor = contentColor;
    }

    public void setContentBoxColor(Color contentBoxColor) {
        this.contentBoxColor = contentBoxColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    @Override
    public void resize(Viewport r) {
        relativeX += (elementShape.getX() * r.getX());
        relativeY += (elementShape.getHeight() * r.getY());
    }

    @Override
    public void focus() {
    }

    @Override
    public void unfocus() {
    }

    @Override
    public void press() {
        if (editable) {
            cursorPosition = content.length();
            contentColor = Color.BLUE;
            contentBoxColor = Color.WHITE;
            cursorColor = Color.BLACK;
            textChanged = true;
        }
    }

    @Override
    public void unpress() {
        contentColor = Color.WHITE;
        contentBoxColor = INVISIBLE;
        cursorColor = INVISIBLE;
    }

    public void notEditable() {
        editable = false;
    }

    public void backup() {
        backup = content;
    }

    public void abordModification() {
        content = backup;
    }

    public boolean hasChanged() {
        return !backup.equals(content);
    }

    private boolean processKeyAction(KeyEvent ke) {
        boolean foundAction = false;
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                deleteBeforeCursor();
                foundAction = true;
                break;
            case KeyEvent.VK_END:
                cursorPosition = content.length();
                foundAction = true;
                break;
            case KeyEvent.VK_HOME:
                cursorPosition = 0;
                foundAction = true;
                break;
            case KeyEvent.VK_DELETE:
                deleteAfterCursor();
                foundAction = true;
                break;
        }
        return foundAction;
    }

    public void processKeyPressed(KeyEvent ke) {
        if (!processKeyAction(ke)) {
            if (isWrittableCharacter(ke)) {
                String beforeCursor = content.substring(0, cursorPosition);
                String afterCursor = content.substring(cursorPosition);
                content = beforeCursor.concat(String.valueOf(ke.getKeyChar())).concat(afterCursor);
                cursorPosition++;
            }
        }
        textChanged = true;
    }

    public void moveCursorRight() {
        if (cursorPosition < content.length()) {
            cursorPosition++;
            textChanged = true;
        }
    }

    public void moveCursorLeft() {
        if (cursorPosition > 0) {
            cursorPosition--;
            textChanged = true;
        }
    }

    private boolean isWrittableCharacter(KeyEvent ke) {
        int code = ke.getKeyCode();
        return code >= 32 && code <= 254;
    }

    private void deleteBeforeCursor() {
        if (cursorPosition > 0) {
            String beforeCursor = content.substring(0, cursorPosition - 1);
            String afterCursor = content.substring(cursorPosition);
            content = beforeCursor.concat(afterCursor);
            cursorPosition--;
        }
    }

    private void deleteAfterCursor() {
        if (cursorPosition < content.length()) {
            String beforeCursor = content.substring(0, cursorPosition);
            String afterCursor = content.substring(cursorPosition + 1);
            content = beforeCursor.concat(afterCursor);
        }
    }

    public void setEditable(boolean b) {
        editable = b;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
