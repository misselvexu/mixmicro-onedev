package io.onedev.server.util.inputspec.showcondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import io.onedev.server.util.OneContext;
import io.onedev.server.util.editable.annotation.ChoiceProvider;
import io.onedev.server.util.editable.annotation.Editable;
import io.onedev.server.util.editable.annotation.OmitName;
import io.onedev.server.util.inputspec.InputSpec;

@Editable
public class ShowCondition implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String inputName;
	
	private ValueMatcher valueMatcher = new ValueIsOneOf();
	
	@Editable(order=100, name="resource.input.whenInput")
	@ChoiceProvider("getNameChoices")
	@NotEmpty
	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	@Editable(order=200)
	@OmitName
	@NotNull(message="may not be empty")
	public ValueMatcher getValueMatcher() {
		return valueMatcher;
	}

	public void setValueMatcher(ValueMatcher valueMatcher) {
		this.valueMatcher = valueMatcher;
	}

	@SuppressWarnings("unused")
	private static List<String> getNameChoices() {
		return new ArrayList<>(OneContext.get().getInputContext().getInputNames());
	}
	
	public boolean isVisible() {
		InputSpec input = OneContext.get().getInputContext().getInput(getInputName());
		Object inputValue = OneContext.get().getEditContext().getInputValue(getInputName());
		if (inputValue != null) {
			List<String> strings = input.convertToStrings(inputValue);
			if (strings.isEmpty())
				return getValueMatcher().matches(null);
			else if (strings.size() == 1)
				return getValueMatcher().matches(strings.iterator().next());
			else 
				throw new IllegalStateException("Show condition should not be based on a multi-value input");
		} else {
			return getValueMatcher().matches(null);
		}
	}
	
}