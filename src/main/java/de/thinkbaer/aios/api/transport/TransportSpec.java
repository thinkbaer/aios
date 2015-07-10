package de.thinkbaer.aios.api.transport;

import java.util.Random;

import de.thinkbaer.aios.api.action.support.OperationRequest;

public class TransportSpec {

	// namespace of handler
	private String ns;
	
	private boolean req;
	
	// operation to execute
	private String op;
	
	// requestId
	private int rid;

	// flags
	private byte[] flags;

	public TransportSpec() {}
	
	public TransportSpec(Transport transport) {
		de.thinkbaer.aios.api.annotation.TransportSpec spec = transport.getClass().getAnnotation(de.thinkbaer.aios.api.annotation.TransportSpec.class);
		setNs(spec.ns());
		setReq(transport instanceof OperationRequest);	
		Random rand = new Random();
		setRid(rand.nextInt(Integer.MAX_VALUE));
		// TODO Auto-generated constructor stub
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public byte[] getFlags() {
		return flags;
	}

	public void setFlags(byte[] flags) {
		this.flags = flags;
	}

	public String toString(){
		return "[Desc ns=" + getNs() + " op="+getOp() + " rid="+ getRid() + " flags="+getFlags()+"]";
	}
	
	public TransportSpec clone(){
		TransportSpec ed = new TransportSpec();
		ed.setNs(getNs());
		ed.setOp(getOp());
		return ed;
	}

	public boolean isReq() {
		return req;
	}

	public void setReq(boolean req) {
		this.req = req;
	}
	
}
