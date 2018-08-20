package br.net.buzu.poc.model;

import java.io.Serializable;

/**
 * Immutable DTO
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Request implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final String service;
	private final String message;
	private final int size;
	private final double price;
	private final boolean active;

	public Request(String service, String message, short size, float price, boolean active) {
		super();
		if (service == null) {
			throw new NullPointerException();
		}
		this.service = service;
		this.message = message;
		this.size = size;
		this.price=price;
		this.active=active;
	}
	
	public Request(String service, String message, short size) {
		super();
		if (service == null) {
			throw new NullPointerException();
		}
		this.service = service;
		this.message = message;
		this.size = size;
		this.price=5.5;
		this.active=true;
	}


	public String getService() {
		return service;
	}

	public String getMessage() {
		return message;
	}
	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "service=" + service + " message="+message + " size:"+size + " price:" + price + " active:" + active;
	}

}
