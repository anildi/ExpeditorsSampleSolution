/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package expeditors.backend.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class AvroPet extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7731237437655562555L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroPet\",\"namespace\":\"expeditors.backend.avro\",\"fields\":[{\"name\":\"pet_type\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Type of Animal\"},{\"name\":\"pet_name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Pet Name, if known\",\"default\":\"\"},{\"name\":\"pet_breed\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Pet Breed, if known (e.g. siamese, dalmation)\",\"default\":\"\"},{\"name\":\"adoption_date\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Date of adoption, if known\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<AvroPet> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<AvroPet> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<AvroPet> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<AvroPet> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<AvroPet> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this AvroPet to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a AvroPet from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a AvroPet instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static AvroPet fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Type of Animal */
  private java.lang.String pet_type;
  /** Pet Name, if known */
  private java.lang.String pet_name;
  /** Pet Breed, if known (e.g. siamese, dalmation) */
  private java.lang.String pet_breed;
  /** Date of adoption, if known */
  private java.lang.String adoption_date;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroPet() {}

  /**
   * All-args constructor.
   * @param pet_type Type of Animal
   * @param pet_name Pet Name, if known
   * @param pet_breed Pet Breed, if known (e.g. siamese, dalmation)
   * @param adoption_date Date of adoption, if known
   */
  public AvroPet(java.lang.String pet_type, java.lang.String pet_name, java.lang.String pet_breed, java.lang.String adoption_date) {
    this.pet_type = pet_type;
    this.pet_name = pet_name;
    this.pet_breed = pet_breed;
    this.adoption_date = adoption_date;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return pet_type;
    case 1: return pet_name;
    case 2: return pet_breed;
    case 3: return adoption_date;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: pet_type = value$ != null ? value$.toString() : null; break;
    case 1: pet_name = value$ != null ? value$.toString() : null; break;
    case 2: pet_breed = value$ != null ? value$.toString() : null; break;
    case 3: adoption_date = value$ != null ? value$.toString() : null; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'pet_type' field.
   * @return Type of Animal
   */
  public java.lang.String getPetType() {
    return pet_type;
  }


  /**
   * Sets the value of the 'pet_type' field.
   * Type of Animal
   * @param value the value to set.
   */
  public void setPetType(java.lang.String value) {
    this.pet_type = value;
  }

  /**
   * Gets the value of the 'pet_name' field.
   * @return Pet Name, if known
   */
  public java.lang.String getPetName() {
    return pet_name;
  }


  /**
   * Sets the value of the 'pet_name' field.
   * Pet Name, if known
   * @param value the value to set.
   */
  public void setPetName(java.lang.String value) {
    this.pet_name = value;
  }

  /**
   * Gets the value of the 'pet_breed' field.
   * @return Pet Breed, if known (e.g. siamese, dalmation)
   */
  public java.lang.String getPetBreed() {
    return pet_breed;
  }


  /**
   * Sets the value of the 'pet_breed' field.
   * Pet Breed, if known (e.g. siamese, dalmation)
   * @param value the value to set.
   */
  public void setPetBreed(java.lang.String value) {
    this.pet_breed = value;
  }

  /**
   * Gets the value of the 'adoption_date' field.
   * @return Date of adoption, if known
   */
  public java.lang.String getAdoptionDate() {
    return adoption_date;
  }


  /**
   * Sets the value of the 'adoption_date' field.
   * Date of adoption, if known
   * @param value the value to set.
   */
  public void setAdoptionDate(java.lang.String value) {
    this.adoption_date = value;
  }

  /**
   * Creates a new AvroPet RecordBuilder.
   * @return A new AvroPet RecordBuilder
   */
  public static expeditors.backend.avro.AvroPet.Builder newBuilder() {
    return new expeditors.backend.avro.AvroPet.Builder();
  }

  /**
   * Creates a new AvroPet RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroPet RecordBuilder
   */
  public static expeditors.backend.avro.AvroPet.Builder newBuilder(expeditors.backend.avro.AvroPet.Builder other) {
    if (other == null) {
      return new expeditors.backend.avro.AvroPet.Builder();
    } else {
      return new expeditors.backend.avro.AvroPet.Builder(other);
    }
  }

  /**
   * Creates a new AvroPet RecordBuilder by copying an existing AvroPet instance.
   * @param other The existing instance to copy.
   * @return A new AvroPet RecordBuilder
   */
  public static expeditors.backend.avro.AvroPet.Builder newBuilder(expeditors.backend.avro.AvroPet other) {
    if (other == null) {
      return new expeditors.backend.avro.AvroPet.Builder();
    } else {
      return new expeditors.backend.avro.AvroPet.Builder(other);
    }
  }

  /**
   * RecordBuilder for AvroPet instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroPet>
    implements org.apache.avro.data.RecordBuilder<AvroPet> {

    /** Type of Animal */
    private java.lang.String pet_type;
    /** Pet Name, if known */
    private java.lang.String pet_name;
    /** Pet Breed, if known (e.g. siamese, dalmation) */
    private java.lang.String pet_breed;
    /** Date of adoption, if known */
    private java.lang.String adoption_date;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(expeditors.backend.avro.AvroPet.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.pet_type)) {
        this.pet_type = data().deepCopy(fields()[0].schema(), other.pet_type);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.pet_name)) {
        this.pet_name = data().deepCopy(fields()[1].schema(), other.pet_name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.pet_breed)) {
        this.pet_breed = data().deepCopy(fields()[2].schema(), other.pet_breed);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.adoption_date)) {
        this.adoption_date = data().deepCopy(fields()[3].schema(), other.adoption_date);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing AvroPet instance
     * @param other The existing instance to copy.
     */
    private Builder(expeditors.backend.avro.AvroPet other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.pet_type)) {
        this.pet_type = data().deepCopy(fields()[0].schema(), other.pet_type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.pet_name)) {
        this.pet_name = data().deepCopy(fields()[1].schema(), other.pet_name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.pet_breed)) {
        this.pet_breed = data().deepCopy(fields()[2].schema(), other.pet_breed);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.adoption_date)) {
        this.adoption_date = data().deepCopy(fields()[3].schema(), other.adoption_date);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'pet_type' field.
      * Type of Animal
      * @return The value.
      */
    public java.lang.String getPetType() {
      return pet_type;
    }


    /**
      * Sets the value of the 'pet_type' field.
      * Type of Animal
      * @param value The value of 'pet_type'.
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder setPetType(java.lang.String value) {
      validate(fields()[0], value);
      this.pet_type = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'pet_type' field has been set.
      * Type of Animal
      * @return True if the 'pet_type' field has been set, false otherwise.
      */
    public boolean hasPetType() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'pet_type' field.
      * Type of Animal
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder clearPetType() {
      pet_type = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'pet_name' field.
      * Pet Name, if known
      * @return The value.
      */
    public java.lang.String getPetName() {
      return pet_name;
    }


    /**
      * Sets the value of the 'pet_name' field.
      * Pet Name, if known
      * @param value The value of 'pet_name'.
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder setPetName(java.lang.String value) {
      validate(fields()[1], value);
      this.pet_name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'pet_name' field has been set.
      * Pet Name, if known
      * @return True if the 'pet_name' field has been set, false otherwise.
      */
    public boolean hasPetName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'pet_name' field.
      * Pet Name, if known
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder clearPetName() {
      pet_name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'pet_breed' field.
      * Pet Breed, if known (e.g. siamese, dalmation)
      * @return The value.
      */
    public java.lang.String getPetBreed() {
      return pet_breed;
    }


    /**
      * Sets the value of the 'pet_breed' field.
      * Pet Breed, if known (e.g. siamese, dalmation)
      * @param value The value of 'pet_breed'.
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder setPetBreed(java.lang.String value) {
      validate(fields()[2], value);
      this.pet_breed = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'pet_breed' field has been set.
      * Pet Breed, if known (e.g. siamese, dalmation)
      * @return True if the 'pet_breed' field has been set, false otherwise.
      */
    public boolean hasPetBreed() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'pet_breed' field.
      * Pet Breed, if known (e.g. siamese, dalmation)
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder clearPetBreed() {
      pet_breed = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'adoption_date' field.
      * Date of adoption, if known
      * @return The value.
      */
    public java.lang.String getAdoptionDate() {
      return adoption_date;
    }


    /**
      * Sets the value of the 'adoption_date' field.
      * Date of adoption, if known
      * @param value The value of 'adoption_date'.
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder setAdoptionDate(java.lang.String value) {
      validate(fields()[3], value);
      this.adoption_date = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'adoption_date' field has been set.
      * Date of adoption, if known
      * @return True if the 'adoption_date' field has been set, false otherwise.
      */
    public boolean hasAdoptionDate() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'adoption_date' field.
      * Date of adoption, if known
      * @return This builder.
      */
    public expeditors.backend.avro.AvroPet.Builder clearAdoptionDate() {
      adoption_date = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AvroPet build() {
      try {
        AvroPet record = new AvroPet();
        record.pet_type = fieldSetFlags()[0] ? this.pet_type : (java.lang.String) defaultValue(fields()[0]);
        record.pet_name = fieldSetFlags()[1] ? this.pet_name : (java.lang.String) defaultValue(fields()[1]);
        record.pet_breed = fieldSetFlags()[2] ? this.pet_breed : (java.lang.String) defaultValue(fields()[2]);
        record.adoption_date = fieldSetFlags()[3] ? this.adoption_date : (java.lang.String) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<AvroPet>
    WRITER$ = (org.apache.avro.io.DatumWriter<AvroPet>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<AvroPet>
    READER$ = (org.apache.avro.io.DatumReader<AvroPet>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.pet_type);

    out.writeString(this.pet_name);

    out.writeString(this.pet_breed);

    out.writeString(this.adoption_date);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.pet_type = in.readString();

      this.pet_name = in.readString();

      this.pet_breed = in.readString();

      this.adoption_date = in.readString();

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.pet_type = in.readString();
          break;

        case 1:
          this.pet_name = in.readString();
          break;

        case 2:
          this.pet_breed = in.readString();
          break;

        case 3:
          this.adoption_date = in.readString();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










