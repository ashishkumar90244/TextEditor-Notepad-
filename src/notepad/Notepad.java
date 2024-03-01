package notepad;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import javax.swing.filechooser.*;
import java.io.*;

public class Notepad extends JFrame implements ActionListener{
    JTextArea area;
    
    public Notepad(){
        
        setTitle("Notepad");
        
        ImageIcon notepadIcon = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/notepad.png"));
        
        Image icon = notepadIcon.getImage();
        
        setIconImage(icon);
        
        JMenuBar menubar = new JMenuBar();
        
        menubar.setBackground(Color.WHITE);
        
        JMenu file = new JMenu("File");
        
        JMenuItem newdoc = new JMenuItem("New");
        newdoc.addActionListener(this);//METHOD CALL 
        newdoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(this);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        
        JMenuItem save_as = new JMenuItem("Save As");
        save_as.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        
        file.add(newdoc);
        file.add(open);
        file.add(save);
        file.add(save_as);
        file.add(exit);
        
        
        
        file.setFont(new Font("AERIAL",Font.PLAIN,14));
        
        menubar.add(file);
        
        JMenu edit = new JMenu("Edit");
        
        JMenuItem copy  = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        
        
        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectAll);
        
        menubar.add(edit);
        
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem help  = new JMenuItem("Help");
        help.addActionListener(this);
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
        helpMenu.add(help);
       
        
        menubar.add(helpMenu);
        setJMenuBar(menubar);
        
        area = new JTextArea();
        
        area.setFont(new Font("SAN_SERIF",Font.PLAIN,18));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        
        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(BorderFactory.createEmptyBorder());
        
        
        add(pane);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("New")){
            area.setText("");
        }
        else if(ae.getActionCommand().equals("Open")){
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files","txt");
            chooser.addChoosableFileFilter(restrict);
            int action = chooser.showOpenDialog(this);
            
            if(action != JFileChooser.APPROVE_OPTION){
                return;
            }
            
            File file = chooser.getSelectedFile();
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                area.read(reader,null);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
            
        }
        else if(ae.getActionCommand().equals("Save")){
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files","txt");
            chooser.setFileFilter(restrict);

            int option = chooser.showSaveDialog(this);
            if(option!=JFileChooser.APPROVE_OPTION)
                return;
            
            File file =new File( chooser.getSelectedFile()+".txt");
            try{
                FileWriter writer = new FileWriter(file);
                writer.write(area.getText());
                writer.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
            
        }
        else if(ae.getActionCommand().equals("Exit")){
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want ot exit", "Exit",JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
                System.exit(0);
        }
        else if(ae.getActionCommand().equals("Copy")){
            
            String selectedText = area.getSelectedText();
            if(selectedText !=null && !selectedText.isEmpty()){
                StringSelection selection = new StringSelection(selectedText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                
            }
        }
        
        else if(ae.getActionCommand().equals("Paste")){
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(this);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    String textToPaste = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    int caretPosition = area.getCaretPosition();
                    area.insert(textToPaste, caretPosition);
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error pasting text!");
                }
            }
        }
        else if(ae.getActionCommand().equals("Cut")){
            
            String selectedText = area.getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
                StringSelection selection = new StringSelection(selectedText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                area.replaceSelection("");
                
            }
        }
        
        else if(ae.getActionCommand().equals("Select All")){
            
            area.selectAll();
        }
        else if(ae.getActionCommand().equals("Help")){
            JOptionPane.showMessageDialog(this, "This is a simple Notepad application.\n\nYou can use it to create and edit text documents.\n\nFor further assistance, please refer to the documentation or help resources.");

        }
        
            
    }

   
    public static void main(String[] args) {
        
        Notepad note = new Notepad();
        
              
    }
    
}
