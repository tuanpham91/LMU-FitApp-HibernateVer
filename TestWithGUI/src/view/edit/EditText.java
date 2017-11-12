package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;
import view.FontAwesome;
import view.GuiContainer;

public class EditText extends ArticleElementPane {
	
	@Getter
	private TextArea textArea; 
	private Button boldText, italicText, underlineText;	
	private HBox buttonContainer; 
	String iconBold, iconItalic, iconUnderline;
	
	private static final String ICON_STYLE = "-fx-font-family: 'FontAwesome';-fx-font-size: 14px;";
	
	/**
	 * this is a subclass of the Edit Window, responsible for changing the content of the article
	 * the autocorrect function is also implemented here : "" -> „“ 
	 * Still the "Gedankenstrich" is not implemented, yet
	 * @param text
	 */
	public EditText(String text, NumberBinding elementsWidthBinding) {
		
		super(elementsWidthBinding); 
		
		this.label = new Label("Text Bereich");
		this.label.prefWidthProperty().bind(elementsWidthBinding);;
		this.setGraphic(label);
//		this.setText("Text Bereich");
		
		iconBold = GuiContainer.ICONS.getString(FontAwesome.bold);
		iconItalic = GuiContainer.ICONS.getString(FontAwesome.italic);
		iconUnderline = GuiContainer.ICONS.getString(FontAwesome.underline);
		
		textArea = new TextArea(text); 
		textArea.setWrapText(true);
		textArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				double currentScroll = textArea.getScrollTop(); 
				if (newValue.length() <= 1) {
					String newText = newValue.replace('\"', (char)0x201e); 
					textArea.setText(newText);
				} else {
					String newText = ""; 
					boolean lowerQuote = false; 
					for (int i = 0; i < newValue.length(); i++) {
						if (newValue.charAt(i) == '\"' && lowerQuote) {
							newText += Character.toString((char)0x201e); 
							lowerQuote = true; 
						} else {
							char nextChar = newValue.charAt(i); 
							if (nextChar == '\"') {
								newText += (char)0x201c; 
							} else {
								newText += Character.toString(nextChar); 								
							}
							if (nextChar == ' ' || nextChar == '\n' || nextChar == '\t') {
								lowerQuote = true; 
							} else {
								lowerQuote = false; 
							}
						}
					}
					textArea.setText(newText);
				}
				textArea.setScrollTop(currentScroll);
			}
		});

		boldText = new Button(); 
		boldText.setStyle(ICON_STYLE);
		boldText.setText(iconBold);
		boldText.setOnMouseClicked(e -> {
			markText("[b]", "[/b]");
		});
		
		italicText = new Button(); 
		italicText.setStyle(ICON_STYLE);
		italicText.setText(iconItalic);
		italicText.setOnMouseClicked(e -> {
			markText("[i]", "[/i]");
		});
		
		underlineText = new Button(); 
		underlineText.setStyle(ICON_STYLE);
		underlineText.setText(iconUnderline);
		underlineText.setOnMouseClicked(e -> {
			markText("[u]", "[/u]");
		});	
		
		buttonContainer = new HBox(); 
		buttonContainer.getChildren().addAll(boldText, italicText, underlineText); 
		
		container.getChildren().addAll(textArea, buttonContainer); 
		
	}
	
	public void markText(String openTag, String closeTag) {
		IndexRange selection = textArea.getSelection();
		int start = selection.getStart();
		int end   = selection.getEnd();
		String boldText = textArea.getText().substring(start, end);
		String p = textArea.getText().substring(0, start);
		String s = textArea.getText().substring(end);
		textArea.setText(p + openTag + boldText + closeTag + s);	
	}

}
