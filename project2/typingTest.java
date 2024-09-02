import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class typingTest {

    private static JFrame frame = new JFrame("Typing Test");
    private JPanel typingPanel = new JPanel(new GridBagLayout());
    private JLabel instructions1 = new JLabel("To begin the test, press the start button below.");
    private JLabel instructions2 = new JLabel("Hit the reset button to end the test early.");
    private JLabel instructions3 = new JLabel("You have one minute to type the sentence as many times as you can. Good luck.");
    private JLabel sentenceToType = new JLabel("<html>The quick brown fox jumped over the lazy dog.</html>");
    private JTextArea wordBank = new JTextArea(10, 30);
    private JButton startButton = new JButton("Start");
    private JButton resetButton = new JButton("Reset");
    private JLabel secLeftPrompt = new JLabel("Seconds Left:");
    public static JLabel secondsLeft = new JLabel("60");

    private Timer timer;
    private TimerTask task;
    private TimerTask task2;

    public typingTest() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTest();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTest();
            }
        });
    }

    private void startTest() {
        pickRandomSentence();
        wordBank.setEditable(true);  // Make wordBank editable on starting the test
        wordBank.setText("");  // Clear any previous text in wordBank
        secondsLeft.setText("60");

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                int correctWordCount = calculateWPM();
                String words = correctWordCount == 1 ? " word " : " words ";
                String finalCount = "You typed " + correctWordCount + words + "correctly in 60 seconds.";
                JOptionPane.showMessageDialog(null, finalCount);
                wordBank.setEditable(false);  // Make wordBank non-editable after test ends
            }
        };

        updateSecondsCountdown();
        timer.schedule(task, 60000);
    }

    private void resetTest() {
        try {
            // Cancel any running tasks
            if (task != null) {
                task.cancel();
            }
            if (task2 != null) {
                task2.cancel();
            }
            if (timer != null) {
                timer.cancel();
            }
    
            // Reset the timer and wordBank
            secondsLeft.setText("60");
            wordBank.setText("");  // Clear text in wordBank
            wordBank.setEditable(false);  // Make wordBank non-editable on reset
        } catch (Exception ex) {
            ex.printStackTrace();  // Log any exceptions for debugging
        }
    }
    

    public static void main(String[] args) {
        typingTest ty = new typingTest();
        ty.initializeDisplay();
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(600, 450));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(ty.typingPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateSecondsCountdown() {
        Timer t2 = new Timer();
        task2 = new TimerTask() {
            int secLeft = 59;
            @Override
            public void run() {
                String sec = String.valueOf(secLeft);
                if (secLeft < 10) {
                    sec = "0" + sec;
                }
                secondsLeft.setText(sec);
                secLeft--;

                if (secLeft < 0) {
                    task2.cancel();
                }
            }
        };

        t2.schedule(task2, 1000, 1000);
    }

    private int calculateWPM() {
        String[] correctWords = sentenceToType.getText().replaceAll("<[^>]*>", "").split(" ");
        String[] typedWords = wordBank.getText().trim().split("\\s+");
        int correctWordCount = 0;

        for (int i = 0; i < Math.min(typedWords.length, correctWords.length); i++) {
            if (correctWords[i].equals(typedWords[i])) {
                correctWordCount++;
            }
        }

        return correctWordCount;
    }

    private void pickRandomSentence() {
        Random r = new Random();
        String[] sentences = new String[3];
        sentences[0] = "Maya had always struggled with self-doubt, but gardening gave her peace. One spring, she planted a small garden, nurturing each seed with care. As the flowers began to bloom, vibrant and full of life, she realized that just like her garden, she too was growing and flourishing. The once barren soil was now a riot of color, a reminder that with patience and care, beauty and strength could emerge from within.";
        sentences[1] = "Jasper had spent years dreaming of climbing the mountain that towered over his village. Despite the fear and the voices telling him he couldn’t do it, he trained relentlessly. The day he reached the summit, he stood above the clouds, feeling the cold wind on his face. Looking down at the path he had conquered, Jasper understood that the greatest triumph was not in reaching the top, but in overcoming the doubts that once held him back.";
        sentences[2] = "Sarah accidentally knocked over a precious vase, shattering it into countless pieces. Distraught, she began piecing it back together, but the cracks were inevitable. Instead of hiding them, she filled the cracks with gold, inspired by the Japanese art of Kintsugi. When the vase was whole again, it was more beautiful than before, a testament to resilience and the belief that our scars make us stronger, not weaker.";
        int num = r.nextInt(sentences.length);
        sentenceToType.setText("<html>" + sentences[num] + "</html>");
    }

    private void initializeDisplay()
    {
            
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        instructions1.setForeground(Color.BLUE);
        instructions2.setForeground(Color.BLUE);
        instructions3.setForeground(Color.BLUE);
        sentenceToType.setForeground(Color.RED);
        secondsLeft.setForeground(Color.RED);
    
        wordBank.setEditable(false);  // Initially, wordBank is not editable
        wordBank.setLineWrap(true);
        wordBank.setWrapStyleWord(true);
    
       // Set a smaller preferred size for wordBank
        Dimension wordBankSize = new Dimension(200, 100); // Smaller dimensions
        wordBank.setPreferredSize(wordBankSize);
        wordBank.setMinimumSize(wordBankSize);

        gbc.anchor = GridBagConstraints.NORTH;
        typingPanel.add(instructions1, gbc);
        typingPanel.add(instructions2, gbc);
        typingPanel.add(instructions3, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        typingPanel.add(sentenceToType, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        typingPanel.add(new JScrollPane(wordBank, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        typingPanel.add(startButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        typingPanel.add(resetButton, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        JPanel countdownPanel = new JPanel();
        countdownPanel.add(secLeftPrompt);
        countdownPanel.add(secondsLeft);
        typingPanel.add(countdownPanel, gbc);
    }
}
