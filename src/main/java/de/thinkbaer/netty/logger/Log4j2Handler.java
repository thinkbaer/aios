package de.thinkbaer.netty.logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.assistedinject.AssistedInject;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * A {@link ChannelHandler} that logs all events using a logging framework. By
 * default, all events are logged at <tt>DEBUG</tt> level.
 */
@Sharable
public class Log4j2Handler extends ChannelDuplexHandler {

	private static final Level DEFAULT_LEVEL = Level.WARN;

	protected final Logger logger;

	private final Level level;

	/**
	 * Creates a new instance whose logger name is the fully qualified class
	 * name of the instance with hex dump enabled.
	 */
	public Log4j2Handler() {
		this(DEFAULT_LEVEL);
	}

	/**
	 * Creates a new instance whose logger name is the fully qualified class
	 * name of the instance.
	 *
	 * @param level
	 *            the log level
	 */
	public Log4j2Handler(Level level) {
		if (level == null) {
			throw new NullPointerException("level");
		}

		logger = LogManager.getLogger(getClass());
		this.level = level;

	}

	/**
	 * Creates a new instance with the specified logger name and with hex dump
	 * enabled.
	 */
	@AssistedInject
	public Log4j2Handler(Class<?> clazz) {
		this(clazz, DEFAULT_LEVEL);
	}

	/**
	 * Creates a new instance with the specified logger name.
	 *
	 * @param level
	 *            the log level
	 */
	public Log4j2Handler(Class<?> clazz, Level level) {
		if (clazz == null) {
			throw new NullPointerException("clazz");
		}
		if (level == null) {
			throw new NullPointerException("level");
		}
		logger = LogManager.getLogger(clazz);
		this.level = level;

	}

	/**
	 * Creates a new instance with the specified logger name.
	 */
	public Log4j2Handler(String name) {
		this(name, DEFAULT_LEVEL);
	}

	/**
	 * Creates a new instance with the specified logger name.
	 *
	 * @param level
	 *            the log level
	 */
	public Log4j2Handler(String name, Level level) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		if (level == null) {
			throw new NullPointerException("level");
		}
		logger = LogManager.getLogger(name);
		this.level = level;
	}

	/**
	 * Returns the {@link LogLevel} that this handler uses to log
	 */
	public Level level() {
		return level;
	}

	protected String format(ChannelHandlerContext ctx, String message) {
		String chStr = ctx.channel().toString();
		return new StringBuilder(chStr.length() + message.length() + 1).append(chStr).append(' ').append(message).toString();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug(format(ctx, "REGISTERED"));
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

		logger.debug(format(ctx, "UNREGISTERED"));
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		logger.debug(format(ctx, "ACTIVE"));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		logger.debug(format(ctx, "INACTIVE"));
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		logger.error(format(ctx, "EXCEPTION: " + cause), cause);
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		logger.debug(format(ctx, "USER_EVENT: " + evt));
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {

		logger.debug(format(ctx, "BIND(" + localAddress + ')'));
		super.bind(ctx, localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
			throws Exception {

		logger.debug(format(ctx, "CONNECT(" + remoteAddress + ", " + localAddress + ')'));

		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

		logger.debug(format(ctx, "DISCONNECT()"));

		super.disconnect(ctx, promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

		logger.debug(format(ctx, "CLOSE()"));
		super.close(ctx, promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

		logger.debug(format(ctx, "DEREGISTER()"));
		super.deregister(ctx, promise);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logMessage(ctx, "RECEIVED", msg);
		ctx.fireChannelRead(msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		logMessage(ctx, "WRITE", msg);
		ctx.write(msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.trace(format(ctx, "FLUSH"));
		ctx.flush();
	}

	private void logMessage(ChannelHandlerContext ctx, String eventName, Object msg) {
		logger.trace(format(ctx, formatMessage(eventName, msg)));
	}

	protected String formatMessage(String eventName, Object msg) {
		if (msg instanceof ByteBuf) {
			return formatByteBuf(eventName, (ByteBuf) msg);
		} else if (msg instanceof ByteBufHolder) {
			return formatByteBufHolder(eventName, (ByteBufHolder) msg);
		} else {
			return formatNonByteBuf(eventName, msg);
		}
	}

	/**
	 * Returns a String which contains all details to log the {@link ByteBuf}
	 */
	protected String formatByteBuf(String eventName, ByteBuf msg) {
		int length = msg.readableBytes();
		if (length == 0) {
			StringBuilder buf = new StringBuilder(eventName.length() + 4);
			buf.append(eventName).append(": 0B");
			return buf.toString();
		} else {
			int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
			StringBuilder buf = new StringBuilder(eventName.length() + 2 + 10 + 1 + 2 + rows * 80);

			buf.append(eventName).append(": ").append(length).append('B').append(NEWLINE);
			appendPrettyHexDump(buf, msg);

			return buf.toString();
		}
	}

	/**
	 * Returns a String which contains all details to log the {@link Object}
	 */
	protected String formatNonByteBuf(String eventName, Object msg) {
		return eventName + ": " + msg;
	}

	/**
	 * Returns a String which contains all details to log the
	 * {@link ByteBufHolder}.
	 *
	 * By default this method just delegates to
	 * {@link #formatByteBuf(String, ByteBuf)}, using the content of the
	 * {@link ByteBufHolder}. Sub-classes may override this.
	 */
	protected String formatByteBufHolder(String eventName, ByteBufHolder msg) {
		String msgStr = msg.toString();
		ByteBuf content = msg.content();
		int length = content.readableBytes();
		if (length == 0) {
			StringBuilder buf = new StringBuilder(eventName.length() + 2 + msgStr.length() + 4);
			buf.append(eventName).append(", ").append(msgStr).append(", 0B");
			return buf.toString();
		} else {
			int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
			StringBuilder buf = new StringBuilder(eventName.length() + 2 + msgStr.length() + 2 + 10 + 1 + 2 + rows * 80);

			buf.append(eventName).append(": ").append(msgStr).append(", ").append(length).append('B').append(NEWLINE);
			appendPrettyHexDump(buf, content);

			return buf.toString();
		}
	}
}
