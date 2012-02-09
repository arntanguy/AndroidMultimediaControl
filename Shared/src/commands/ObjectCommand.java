package commands;

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
