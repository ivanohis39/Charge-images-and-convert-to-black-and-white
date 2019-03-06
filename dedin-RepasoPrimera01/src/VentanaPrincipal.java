import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class VentanaPrincipal {

	JFrame frame = new JFrame("Repaso Imagenes");

	JPanel panelSuperior, panelInferior;
	JPanel panelImage;
	JButton butonCargar, buttonBlack;

	// para poder crear la imagen
	BufferedImage canvas = null, imageActual;
	Icon icon;
	Image imagen;

	// nos cramos el label en el cual aniadiremos la imagen
	JLabel foto = new JLabel(icon);

	public VentanaPrincipal() {
		super();
		frame.setBounds(300, 100, 700, 700);
		frame.setMinimumSize(new Dimension(500, 600));
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void inicializarComponentes() {
		/**
		 * Creamos y aniadimos el panel superior al frame
		 */
		GridBagConstraints gb = new GridBagConstraints();
		panelSuperior = new JPanel();
		panelSuperior.setLayout(new GridBagLayout());
		panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		gb.gridx = 0;
		gb.gridy = 0;
		gb.weighty = 2;
		gb.fill = GridBagConstraints.BOTH;
		frame.add(panelSuperior, gb);

		/**
		 * Creamos un panel interior al panel superior y se lo aniadimos
		 */
		panelImage = new JPanel();
		panelImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// Usamos el setPreferredSize y le decimos la dimesion que queremos que tenga
		// nuestro panel central
		panelImage.setPreferredSize(new Dimension(400, 400));
		panelSuperior.add(panelImage);

		panelImage.add(foto);

		/**
		 * Creamos y aniadimos el panel Inferior al frame
		 */
		panelInferior = new JPanel();
		panelInferior.setBackground(Color.CYAN);
		panelInferior.setLayout(new GridLayout());
		gb.gridx = 0;
		gb.gridy = 1;
		gb.weightx = 1;
		gb.weighty = 2;
		gb.fill = GridBagConstraints.BOTH;
		frame.add(panelInferior, gb);

		/**
		 * Creamos y aniadimos los botones al panel inferior
		 */

		butonCargar = new JButton("Cargar");
		panelInferior.add(butonCargar);

		buttonBlack = new JButton("Blanco Y Negro");
		panelInferior.add(buttonBlack);

		try {
			canvas = ImageIO.read(new File("cargar.png"));
			imagen = canvas.getScaledInstance(100, 100, canvas.SCALE_SMOOTH);
			icon = new ImageIcon(imagen);
			butonCargar.setIcon(icon);

			canvas = ImageIO.read(new File("yin.png"));
			imagen = canvas.getScaledInstance(100, 100, canvas.SCALE_SMOOTH);
			icon = new ImageIcon(imagen);
			buttonBlack.setIcon(icon);
			buttonBlack.setHorizontalTextPosition(SwingConstants.LEFT);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void inicializarListeners() {
		butonCargar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setMultiSelectionEnabled(true);
				int result = fileChooser.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					File fichero = fileChooser.getSelectedFile();
					try {
						canvas = ImageIO.read(fichero);
						imagen = canvas.getScaledInstance(panelImage.getWidth() - 2, panelImage.getHeight() - 15,
								canvas.SCALE_SMOOTH);
						icon = new ImageIcon(imagen);
						foto.setIcon(icon);
						panelImage.add(foto);

						frame.repaint();
						imageActual = canvas;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		buttonBlack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int mediaPixel, colorSRGB;
				Color colorAux;

				// Recorremos la imagen píxel a píxel
				for (int i = 0; i < imageActual.getWidth(); i++) {
					for (int j = 0; j < imageActual.getHeight(); j++) {
						// Almacenamos el color del píxel
						colorAux = new Color(imageActual.getRGB(i, j));
						// Calculamos la media de los tres canales (rojo, verde, azul)
						mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
						// Cambiamos a formato sRGB
						colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
						// Asignamos el nuevo valor al BufferedImage
						imageActual.setRGB(i, j, colorSRGB);
						imagen = imageActual.getScaledInstance(panelImage.getWidth(), panelImage.getHeight(),
								canvas.SCALE_SMOOTH);
					}
				}
				foto.setIcon(new ImageIcon(imagen));
			}
		});
	}

	public void inicializar() {
		frame.setVisible(true);
		inicializarComponentes();
		inicializarListeners();
	}
}
