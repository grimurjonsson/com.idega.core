package com.idega.data;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IDORemoveException extends javax.ejb.RemoveException {

  public IDORemoveException() {
  }

  public IDORemoveException(String message){
    super(message);
  }

  public IDORemoveException(Exception forwardException) {
    this(forwardException.getMessage());
  }
}
