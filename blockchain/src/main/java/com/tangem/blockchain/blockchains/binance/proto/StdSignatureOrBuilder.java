// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dex.proto

package com.tangem.blockchain.blockchains.binance.proto;

public interface StdSignatureOrBuilder extends
    // @@protoc_insertion_point(interface_extends:transaction.StdSignature)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * public key bytes of the signer address
   * </pre>
   *
   * <code>bytes pub_key = 1;</code>
   */
  com.google.protobuf.ByteString getPubKey();

  /**
   * <pre>
   * signature bytes, please check chain access section for signature generation
   * </pre>
   *
   * <code>bytes signature = 2;</code>
   */
  com.google.protobuf.ByteString getSignature();

  /**
   * <pre>
   * another identifier of signer, which can be read from chain by account REST API or RPC
   * </pre>
   *
   * <code>int64 account_number = 3;</code>
   */
  long getAccountNumber();

  /**
   * <pre>
   * sequence number for the next transaction of the client, which can be read fro chain by account REST API or RPC. please check chain acces section for details.
   * </pre>
   *
   * <code>int64 sequence = 4;</code>
   */
  long getSequence();
}
