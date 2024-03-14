import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import expeditors.backend.adoptapp.domain.Adopter;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


private JFrame frame;

private JDialog ourDialog;
private JTextField messageTF;
private JTextField topicTF;
private JButton sendButton;
private JButton exitButton;
private Producer<String, Object> producer;
private String topic;
private String messageKey = "adopter";
private ObjectMapper objectMapper = new ObjectMapper();

public void main(String[] args) throws IOException {
   args = new String[]{"kafka_javaclient.properties"};
   if (args.length != 1) {
      System.out.println("Please provide the configuration file path as a command line argument");
      System.exit(1);
   }

   Properties props = loadConfig(args[0]);
   topic = props.getProperty("ttl.kafka.adopter.topic");

   producer = new KafkaProducer<>(props);

   frame = new JFrame("Simple Kafka Message Sender");

   frame.setLocationRelativeTo(null);
   frame.setVisible(true);

//      way1(props, frame);
   way2(props, frame);
}


public void way2(Properties props, JFrame ourFrame) throws IOException {
   ourDialog = createDialog(ourFrame, this::sendMessage, this::handleExitButton);
   ourDialog.setLocationRelativeTo(ourFrame);
   ourDialog.setVisible(true);
}

public void sendMessage(ActionEvent ae) {
   try {
      String newLine = messageTF.getText();
      if (!newLine.isEmpty()) {
         Adopter adopter = objectMapper.readValue(newLine, Adopter.class);
         producer.send(
               //new ProducerRecord<>(topic, messageKey, adopter),
               new ProducerRecord<>(topicTF.getText(), messageKey, adopter),
//                  new ProducerRecord<>(topic, key, newLine),
               (event, ex) -> {
                  if (ex != null)
                     ex.printStackTrace();
                  else
                     System.out.printf("Produced event to topic %s, key %s, value = %s%n", topic, messageKey, newLine);
               });
      }
   } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
   }
}

public void handleExitButton(ActionEvent ae) {
   System.exit(0);
}

public Properties loadConfig(final String configFile) throws IOException {
   final Properties props = new Properties();
   props.load(this.getClass().getClassLoader().getResourceAsStream("kafka_javaclient.properties"));
   return props;
}

private JDialog createDialog(JFrame frame, ActionListener msgHandler,
                             ActionListener exitHandler) {
   JDialog dialog = new JDialog(frame, "SimpleKafkaClient");
   dialog.setLayout(new GridBagLayout());

   JPanel topicPanel = new JPanel();
   topicPanel.setLayout(new GridBagLayout());
   JLabel topicLabel = new JLabel("Topic");
   var gbc = new GridBagConstraints();
   gbc.anchor = GridBagConstraints.EAST;
   gbc.insets = new Insets(0, 0, 0, 5);
   topicPanel.add(topicLabel, gbc);

   topicTF = new JTextField(topic);
   gbc = new GridBagConstraints();
   gbc.gridwidth = GridBagConstraints.REMAINDER;
   gbc.fill = GridBagConstraints.HORIZONTAL;
   gbc.weightx = 1.0;
   topicPanel.add(topicTF, gbc);

   sendButton = new JButton("Send Message");
   sendButton.addActionListener(msgHandler);
   sendButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
         handleEnterKey(msgHandler, e);
      }
   });
   gbc = new GridBagConstraints();
   gbc.insets = new Insets(0, 0, 0, 5);
   gbc.anchor = GridBagConstraints.EAST;
   topicPanel.add(sendButton, gbc);

   messageTF = new JTextField();
   messageTF.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
         handleEnterKey(msgHandler, e);
      }

   });

   gbc = new GridBagConstraints();
   gbc.gridwidth = GridBagConstraints.REMAINDER;
   gbc.fill = GridBagConstraints.HORIZONTAL;
   gbc.weightx = 1.0;
   topicPanel.add(messageTF, gbc);

   gbc = new GridBagConstraints();
   gbc.fill = GridBagConstraints.HORIZONTAL;
   gbc.gridwidth = GridBagConstraints.REMAINDER;
   gbc.weightx = 1.0;
   dialog.add(topicPanel, gbc);

   JPanel buttonPanel = new JPanel(new GridBagLayout());
//   buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
   exitButton = new JButton("Exit");
   exitButton.addActionListener(exitHandler);
   exitButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
         handleEnterKey(exitHandler, e);
      }
   });


   gbc = new GridBagConstraints();
   gbc.anchor = GridBagConstraints.WEST;
   buttonPanel.add(exitButton, gbc);

   gbc = new GridBagConstraints();
   gbc.insets = new Insets(10, 0, 0 , 0);
   gbc.anchor = GridBagConstraints.WEST;
   dialog.add(buttonPanel, gbc);
   dialog.pack();
   return dialog;
}

private void handleEnterKey(ActionListener actionListener, KeyEvent e) {
   if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      var actionEvent = new ActionEvent(exitButton, 10, "Enter Pressed");
      actionListener.actionPerformed(actionEvent);
   }
}

public void way1(Properties props, JFrame ourFrame) throws IOException {
   String line = "";


   try (final Producer<String, Object> producer = new KafkaProducer<>(props);
        var reader = new BufferedReader(new InputStreamReader(System.in));
   ) {
      while (true) {
         line = JOptionPane.showInputDialog(ourFrame, "Enter Msg: ", line);

         if (line == null || line.equalsIgnoreCase("Quit")) {
            System.exit(0);
         }

         if (!line.isEmpty()) {
            String newLine = line;
            Adopter adopter = objectMapper.readValue(newLine, Adopter.class);
            producer.send(
                  new ProducerRecord<>(topic, messageKey, adopter),
//                  new ProducerRecord<>(topic, key, newLine),
                  (event, ex) -> {
                     if (ex != null)
                        ex.printStackTrace();
                     else
                        System.out.printf("Produced event to topic %s, key %s, value = %s%n", topic, messageKey, newLine);
                  });
         }
      }
   }

}
