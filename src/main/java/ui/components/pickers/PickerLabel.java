package ui.components.pickers;

import java.util.List;
import java.util.Optional;

import backend.resource.TurboLabel;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

// for use with LabelPickerDialog
public class PickerLabel extends TurboLabel {

    private boolean isSelected;
    private boolean isHighlighted;
    private boolean isRemoved;
    private boolean isFaded;
    private final boolean isTop;

    public PickerLabel(TurboLabel label, boolean isTop) {
        super(label.getRepoId(), label.getColour(), label.getActualName());
        isSelected = false;
        isHighlighted = false;
        isRemoved = false;
        isFaded = false;
        this.isTop = isTop;
    }

    public PickerLabel(TurboLabel label, boolean isSelected, boolean isHighlighted, 
                       boolean isRemoved, boolean isFaded, boolean isTop) {
        super(label.getRepoId(), label.getColour(), label.getActualName());
        this.isSelected = isSelected;
        this.isHighlighted = isHighlighted;
        this.isRemoved = isRemoved;
        this.isFaded = isFaded;
        this.isTop = isTop;
    }

    @Override
    public Node getNode() {
        // actual name for labels at the top, add tick for selected labels
        Label label = new Label((isTop ? getActualName() : getName()));
        label.getStyleClass().add("labels");
        if (isRemoved) label.getStyleClass().add("labels-removed"); // add strikethrough
        String style = getStyle() + (isHighlighted ? " -fx-border-color: black;" : ""); // add highlight border
        style += (isFaded ? " -fx-opacity: 40%;" : ""); // change opacity if needed
        label.setStyle(style);

        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double width = (double) fontLoader.computeStringWidth(label.getText(), label.getFont());
        label.setPrefWidth(width + 30);
        label.setText(label.getText() + (!isTop && isSelected ? " ✓" : ""));

        if (getGroup().isPresent()) {
            Tooltip groupTooltip = new Tooltip(getGroup().get());
            label.setTooltip(groupTooltip);
        }

        //label.setOnMouseClicked(e -> labelPickerUILogic.toggleLabel(getActualName()));
        return label;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setIsHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public boolean isFaded() {
        return isFaded;
    }

    public void setIsFaded(boolean isFaded) {
        this.isFaded = isFaded;
    }

    public Node processAssignedLabel(List<String> assignedLabels, Optional<String> suggestion) {
        if (isSuggested(suggestion) && !assignedLabels.contains(getActualName())) {
            setIsFaded(true);
            return getNode();
        }
        
        if (!assignedLabels.contains(getActualName())) {
           setIsRemoved(true); 
           setIsFaded(true);
           return getNode();
        }
        return getNode();
    }

    /**
     * This isn't unnecessary as fields are added, but are not taken into account for equality.
     * @return
     */
    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * This isn't unnecessary as fields are added, but are not taken into account for equality.
     * @return
     */
    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        return super.hashCode();
    }
    
    private boolean isSuggested(Optional<String> suggestion) {
        return suggestion.isPresent() && suggestion.get().equals(getActualName());
    }
}
