package commands;

/**
 * A generic command classed, used to pass not only the name of the command, but
 * also a serializable object as a parameter.
 * 
 * @author TANGUY Arnaud
 * 
 * @param <T>
 *            the type of the object used as an argument. WARNING: T is meant to
 *            be serializable, otherwise it won't be able to pass though the
 *            socket!
 */
public class ObjectCommand<T> extends Command {
	private static final long serialVersionUID = 1L;
	private T object;

	public ObjectCommand(CommandWord command, T t) {
		super(command);
		object = t;
	}

	public ObjectCommand(CommandWord command) {
		this(command, null);
	}

	public ObjectCommand() {
		this(CommandWord.UNKNOWN);
	}

	public T getObject() {
		return object;
	}

	public void setObject(T o) {
		object = o;
	}
}
