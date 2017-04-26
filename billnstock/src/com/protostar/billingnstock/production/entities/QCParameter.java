package com.protostar.billingnstock.production.entities;

import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.stock.entities.StockItemUnit;
import com.protostar.billnstock.until.data.Constants.QCParameterType;

public class QCParameter {
	private String name;
	private QCParameterType parameterType = QCParameterType.NUMBER;
	private float numberIdealValue = 25f;
	private float numberIdealValueValidDeviationPerc = 2.5f;
	private float numberValidRangeFrom = 0f;
	private float numberValidRangeTo = 100f;

	private boolean booleanIdealValue = false;
	private String textValue;

	private Ref<MachineQCUnitMeasure> paramUnit;
	private Ref<StockItemUnit> itemUnit;

	public MachineQCUnitMeasure getParamUnit() {
		return paramUnit == null ? null : paramUnit.get();
	}

	public void setParamUnit(MachineQCUnitMeasure paramUnit) {
		if (paramUnit != null)
			this.paramUnit = Ref.create(paramUnit);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getNumberIdealValue() {
		return numberIdealValue;
	}

	public void setNumberIdealValue(float numberIdealValue) {
		this.numberIdealValue = numberIdealValue;
	}

	public float getNumberIdealValueValidDeviationPerc() {
		return numberIdealValueValidDeviationPerc;
	}

	public void setNumberIdealValueValidDeviationPerc(float numberIdealValueValidDeviationPerc) {
		this.numberIdealValueValidDeviationPerc = numberIdealValueValidDeviationPerc;
	}

	public float getNumberValidRangeFrom() {
		return numberValidRangeFrom;
	}

	public void setNumberValidRangeFrom(float numberValidRangeFrom) {
		this.numberValidRangeFrom = numberValidRangeFrom;
	}

	public float getNumberValidRangeTo() {
		return numberValidRangeTo;
	}

	public void setNumberValidRangeTo(float numberValidRangeTo) {
		this.numberValidRangeTo = numberValidRangeTo;
	}

	public boolean isBooleanIdealValue() {
		return booleanIdealValue;
	}

	public void setBooleanIdealValue(boolean booleanIdealValue) {
		this.booleanIdealValue = booleanIdealValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public QCParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(QCParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public StockItemUnit getItemUnit() {
		return itemUnit == null ? null : itemUnit.get();
	}

	public void setItemUnit(StockItemUnit itemUnit) {
		this.itemUnit = Ref.create(itemUnit);
	}
}