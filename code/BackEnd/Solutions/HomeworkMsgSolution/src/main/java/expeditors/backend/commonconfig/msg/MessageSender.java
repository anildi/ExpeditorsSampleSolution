package expeditors.backend.commonconfig.msg;

/**
 * @author whynot
 */
public interface MessageSender {
    public void sendMessage(Object message);
    default public void sendMessage(Object message, String topic) {
        throw new UnsupportedOperationException("Needs implementing");
    }
}
