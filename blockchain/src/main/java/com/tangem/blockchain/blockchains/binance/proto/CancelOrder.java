// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dex.proto

package com.tangem.blockchain.blockchains.binance.proto;

/**
 * <pre>
 * please note the field name is the JSON name.
 * </pre>
 *
 * Protobuf type {@code transaction.CancelOrder}
 */
public  final class CancelOrder extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:transaction.CancelOrder)
    CancelOrderOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CancelOrder.newBuilder() to construct.
  private CancelOrder(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CancelOrder() {
    sender_ = com.google.protobuf.ByteString.EMPTY;
    symbol_ = "";
    refid_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CancelOrder(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {

            sender_ = input.readBytes();
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            symbol_ = s;
            break;
          }
          case 26: {
            String s = input.readStringRequireUtf8();

            refid_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.tangem.blockchain.blockchains.binance.proto.Transaction.internal_static_transaction_CancelOrder_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.tangem.blockchain.blockchains.binance.proto.Transaction.internal_static_transaction_CancelOrder_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.tangem.blockchain.blockchains.binance.proto.CancelOrder.class, com.tangem.blockchain.blockchains.binance.proto.CancelOrder.Builder.class);
  }

  public static final int SENDER_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString sender_;
  /**
   * <pre>
   *    0x166E681B   // hardcoded, object type prefix in 4 bytes
   * </pre>
   *
   * <code>bytes sender = 1;</code>
   */
  public com.google.protobuf.ByteString getSender() {
    return sender_;
  }

  public static final int SYMBOL_FIELD_NUMBER = 2;
  private volatile Object symbol_;
  /**
   * <pre>
   * symbol for trading pair in full name of the tokens
   * </pre>
   *
   * <code>string symbol = 2;</code>
   */
  public String getSymbol() {
    Object ref = symbol_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      symbol_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * symbol for trading pair in full name of the tokens
   * </pre>
   *
   * <code>string symbol = 2;</code>
   */
  public com.google.protobuf.ByteString
      getSymbolBytes() {
    Object ref = symbol_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      symbol_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REFID_FIELD_NUMBER = 3;
  private volatile Object refid_;
  /**
   * <pre>
   * order id of the one to cancel
   * </pre>
   *
   * <code>string refid = 3;</code>
   */
  public String getRefid() {
    Object ref = refid_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      refid_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * order id of the one to cancel
   * </pre>
   *
   * <code>string refid = 3;</code>
   */
  public com.google.protobuf.ByteString
      getRefidBytes() {
    Object ref = refid_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      refid_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!sender_.isEmpty()) {
      output.writeBytes(1, sender_);
    }
    if (!getSymbolBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, symbol_);
    }
    if (!getRefidBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, refid_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!sender_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(1, sender_);
    }
    if (!getSymbolBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, symbol_);
    }
    if (!getRefidBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, refid_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.tangem.blockchain.blockchains.binance.proto.CancelOrder)) {
      return super.equals(obj);
    }
    com.tangem.blockchain.blockchains.binance.proto.CancelOrder other = (com.tangem.blockchain.blockchains.binance.proto.CancelOrder) obj;

    if (!getSender()
        .equals(other.getSender())) return false;
    if (!getSymbol()
        .equals(other.getSymbol())) return false;
    if (!getRefid()
        .equals(other.getRefid())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SENDER_FIELD_NUMBER;
    hash = (53 * hash) + getSender().hashCode();
    hash = (37 * hash) + SYMBOL_FIELD_NUMBER;
    hash = (53 * hash) + getSymbol().hashCode();
    hash = (37 * hash) + REFID_FIELD_NUMBER;
    hash = (53 * hash) + getRefid().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.tangem.blockchain.blockchains.binance.proto.CancelOrder prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * please note the field name is the JSON name.
   * </pre>
   *
   * Protobuf type {@code transaction.CancelOrder}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:transaction.CancelOrder)
      com.tangem.blockchain.blockchains.binance.proto.CancelOrderOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.tangem.blockchain.blockchains.binance.proto.Transaction.internal_static_transaction_CancelOrder_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.tangem.blockchain.blockchains.binance.proto.Transaction.internal_static_transaction_CancelOrder_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.tangem.blockchain.blockchains.binance.proto.CancelOrder.class, com.tangem.blockchain.blockchains.binance.proto.CancelOrder.Builder.class);
    }

    // Construct using com.tangem.blockchain.blockchains.binance.proto.CancelOrder.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      sender_ = com.google.protobuf.ByteString.EMPTY;

      symbol_ = "";

      refid_ = "";

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.tangem.blockchain.blockchains.binance.proto.Transaction.internal_static_transaction_CancelOrder_descriptor;
    }

    @Override
    public com.tangem.blockchain.blockchains.binance.proto.CancelOrder getDefaultInstanceForType() {
      return com.tangem.blockchain.blockchains.binance.proto.CancelOrder.getDefaultInstance();
    }

    @Override
    public com.tangem.blockchain.blockchains.binance.proto.CancelOrder build() {
      com.tangem.blockchain.blockchains.binance.proto.CancelOrder result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public com.tangem.blockchain.blockchains.binance.proto.CancelOrder buildPartial() {
      com.tangem.blockchain.blockchains.binance.proto.CancelOrder result = new com.tangem.blockchain.blockchains.binance.proto.CancelOrder(this);
      result.sender_ = sender_;
      result.symbol_ = symbol_;
      result.refid_ = refid_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.tangem.blockchain.blockchains.binance.proto.CancelOrder) {
        return mergeFrom((com.tangem.blockchain.blockchains.binance.proto.CancelOrder)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.tangem.blockchain.blockchains.binance.proto.CancelOrder other) {
      if (other == com.tangem.blockchain.blockchains.binance.proto.CancelOrder.getDefaultInstance()) return this;
      if (other.getSender() != com.google.protobuf.ByteString.EMPTY) {
        setSender(other.getSender());
      }
      if (!other.getSymbol().isEmpty()) {
        symbol_ = other.symbol_;
        onChanged();
      }
      if (!other.getRefid().isEmpty()) {
        refid_ = other.refid_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.tangem.blockchain.blockchains.binance.proto.CancelOrder parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.tangem.blockchain.blockchains.binance.proto.CancelOrder) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.protobuf.ByteString sender_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     *    0x166E681B   // hardcoded, object type prefix in 4 bytes
     * </pre>
     *
     * <code>bytes sender = 1;</code>
     */
    public com.google.protobuf.ByteString getSender() {
      return sender_;
    }
    /**
     * <pre>
     *    0x166E681B   // hardcoded, object type prefix in 4 bytes
     * </pre>
     *
     * <code>bytes sender = 1;</code>
     */
    public Builder setSender(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }

      sender_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *    0x166E681B   // hardcoded, object type prefix in 4 bytes
     * </pre>
     *
     * <code>bytes sender = 1;</code>
     */
    public Builder clearSender() {

      sender_ = getDefaultInstance().getSender();
      onChanged();
      return this;
    }

    private Object symbol_ = "";
    /**
     * <pre>
     * symbol for trading pair in full name of the tokens
     * </pre>
     *
     * <code>string symbol = 2;</code>
     */
    public String getSymbol() {
      Object ref = symbol_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        symbol_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * symbol for trading pair in full name of the tokens
     * </pre>
     *
     * <code>string symbol = 2;</code>
     */
    public com.google.protobuf.ByteString
        getSymbolBytes() {
      Object ref = symbol_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        symbol_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * symbol for trading pair in full name of the tokens
     * </pre>
     *
     * <code>string symbol = 2;</code>
     */
    public Builder setSymbol(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      symbol_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * symbol for trading pair in full name of the tokens
     * </pre>
     *
     * <code>string symbol = 2;</code>
     */
    public Builder clearSymbol() {

      symbol_ = getDefaultInstance().getSymbol();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * symbol for trading pair in full name of the tokens
     * </pre>
     *
     * <code>string symbol = 2;</code>
     */
    public Builder setSymbolBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      symbol_ = value;
      onChanged();
      return this;
    }

    private Object refid_ = "";
    /**
     * <pre>
     * order id of the one to cancel
     * </pre>
     *
     * <code>string refid = 3;</code>
     */
    public String getRefid() {
      Object ref = refid_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        refid_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * order id of the one to cancel
     * </pre>
     *
     * <code>string refid = 3;</code>
     */
    public com.google.protobuf.ByteString
        getRefidBytes() {
      Object ref = refid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        refid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * order id of the one to cancel
     * </pre>
     *
     * <code>string refid = 3;</code>
     */
    public Builder setRefid(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      refid_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * order id of the one to cancel
     * </pre>
     *
     * <code>string refid = 3;</code>
     */
    public Builder clearRefid() {

      refid_ = getDefaultInstance().getRefid();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * order id of the one to cancel
     * </pre>
     *
     * <code>string refid = 3;</code>
     */
    public Builder setRefidBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      refid_ = value;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:transaction.CancelOrder)
  }

  // @@protoc_insertion_point(class_scope:transaction.CancelOrder)
  private static final com.tangem.blockchain.blockchains.binance.proto.CancelOrder DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.tangem.blockchain.blockchains.binance.proto.CancelOrder();
  }

  public static com.tangem.blockchain.blockchains.binance.proto.CancelOrder getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CancelOrder>
      PARSER = new com.google.protobuf.AbstractParser<CancelOrder>() {
    @Override
    public CancelOrder parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CancelOrder(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CancelOrder> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<CancelOrder> getParserForType() {
    return PARSER;
  }

  @Override
  public com.tangem.blockchain.blockchains.binance.proto.CancelOrder getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

