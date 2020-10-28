/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
//ghj
/**
 *
 * @author fwmar
 */
public class Filtros implements ActionListener
{
    
    JMenu menu, filtros, submenu;  
    JMenuItem m1o1, m1o2, m1o3, m1o4, m1o5, m2o1, m2o2, m2o3, m2o4, m2o5; 
         
    public JMenuBar Menu(){
        JMenuBar mb=new JMenuBar();  
        menu=new JMenu("Arquivo");  
        submenu=new JMenu("Sub Menu");  
        m1o1=new JMenuItem("Abrir Imagem"); 
        m1o1.addActionListener(this);
        m1o2=new JMenuItem("Salvar Imagem");  
        m1o3=new JMenuItem("Fechar");  
        //i4=new JMenuItem("Item 4");  
        //i5=new JMenuItem("Item 5");  
        menu.add(m1o1); menu.add(m1o2); menu.add(m1o3);  
       
        
        filtros=new JMenu("Efeitos");  
        m2o1=new JMenuItem("Preto e Branco");
        m2o1.addActionListener(this);
        m2o2=new JMenuItem("Escala de Cinza"); 
        m2o2.addActionListener(this);
        m2o3=new JMenuItem("Negativo");
        m2o3.addActionListener(this);
        m2o4=new JMenuItem("Mediana");
        m2o4.addActionListener(this);
        filtros.add(m2o1); filtros.add(m2o2); filtros.add(m2o3); filtros.add(m2o4);
        
        
        //submenu.add(i4); submenu.add(i5);  
        //menu.add(submenu);  
        mb.add(menu);
        mb.add(filtros);
        return mb;
    }
    
    
    public void actionPerformed(ActionEvent e) {  
        Object obj=e.getSource();
	
        if(obj==m1o1){
            abrirImagem();
        }
        if(obj==m2o1){
            image2BlackWhite(origImage);
        }
        if(obj==m2o2){
            image2GrayScale(origImage);
        }
        if(obj==m2o3){
            negativo(origImage);
        }
        if(obj==m2o4){
            String nivel = JOptionPane.showInputDialog(null, "Digite o Nível da Mediana");
            int intNivel = Integer.parseInt(nivel);
            mediana(origImage,intNivel);
        }
    }
    
    JFrame janela = new JFrame();
    JLabel label = new JLabel();
    BufferedImage origImage = null;
    
    public void Tela(){
        janela.setJMenuBar(Menu());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        janela.setSize(640, 480); 
        janela.setVisible(true);    
    }
    
    public void abrirImagem(){
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Please choose an image...");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg", "jpg", "png", "bmp", "gif");
        fc.addChoosableFileFilter(filter);
        // You should use the parent component instead of null
        // but it was impossible to tell from the code snippet what that was.
        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            try {
                origImage = ImageIO.read(selectedFile);
                ImageIcon image = new ImageIcon(origImage);
                label.setIcon(image);
                System.out.println(selectedFile);
                janela.getContentPane().add(label);
                janela.setLocationRelativeTo(null);
                janela.pack();
                janela.setVisible(true);
                label.repaint();
                janela.repaint();
                //janela.setSize(640, 480);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void image2BlackWhite(BufferedImage origImage){
		int w = origImage.getWidth();
		int h = origImage.getHeight();
		byte[] comp = { 0, -1 };
		IndexColorModel cm = new IndexColorModel(2, 2, comp, comp, comp);
		BufferedImage image2 = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_INDEXED, cm);
		Graphics2D g = image2.createGraphics();
		g.drawRenderedImage(origImage, null);
		g.dispose();

		mostraEfeito(image2);
    }
    
    public void image2GrayScale(BufferedImage origImage) {
		BufferedImage imageGray = new BufferedImage(origImage.getWidth(), origImage.getHeight(),BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = imageGray.getGraphics();  
		g.drawImage(origImage, 0, 0, null);  
		g.dispose();
                mostraEfeito(imageGray);
    }
    
    public void negativo(BufferedImage origImage1) {
        int width = origImage1.getWidth();
        int height = origImage1.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {               
                int rgb = origImage1.getRGB(i, j);               //a cor inversa é dado por 255 menos o valor da cor                 
                int r = 255 - (int)((rgb&0x00FF0000)>>>16);
                int g = 255 - (int)((rgb&0x0000FF00)>>>8);
                int b = 255 - (int) (rgb&0x000000FF);
                Color color = new Color(r, g, b);
                origImage1.setRGB(i, j, color.getRGB());
            }
        }
        mostraEfeito(origImage1);
    }
    
    public void mediana(BufferedImage image, int limiar) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {               
                int rgb = image.getRGB(i, j);               
                int r = (int)((rgb&0x00FF0000)>>>16);
                int g = (int)((rgb&0x0000FF00)>>>8);
                int b = (int) (rgb&0x000000FF);
                int media = (r + g + b) / 3;
                Color white = new Color(255, 255, 255);
                Color black = new Color(0, 0, 0);
                //como explicado no artigo, no threshold definimos um limiar,
                //que é um valor "divisor de águas"
                //pixels com valor ABAIXO do limiar viram pixels PRETOS,
                //pixels com valor ACIMA do limiar viram pixels BRANCOS
                if (media < limiar)
                    image.setRGB(i, j, black.getRGB());
                else
                    image.setRGB(i, j, white.getRGB());
            }
        }
        mostraEfeito(image);
    }
    
    public void mostraEfeito(BufferedImage imagem){
                ImageIcon image = new ImageIcon(imagem);
                label.setIcon(image);
                janela.getContentPane().add(label);
                janela.setLocationRelativeTo(null);
                janela.pack();
                janela.setVisible(true);
                label.repaint();
                janela.repaint();
    }
    
    
}
