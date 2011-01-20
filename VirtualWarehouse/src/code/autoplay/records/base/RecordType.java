package code.autoplay.records.base;

import java.io.Serializable;

/**
 * Defines an enumeration delineating the different "types" of record entries. This is enumeration
 * is somewhat redundant since each record entry type has its own class. However, this allows for 
 * the use of switch statements and quicker identification. 
 * 
 * @author jahinshaw
 *
 */
public enum RecordType implements Serializable
{
	TRANSFORMATION, PARENT_CHANGE, END_RECORD, CREATE_PRODUCT, IMU_TRANSFORM;
}
