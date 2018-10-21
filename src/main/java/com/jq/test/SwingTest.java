package com.jq.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SwingTest {
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame f = new JFrame("测试");
		f.setSize(500, 500);
        // 创建及设置窗口
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        JPanel p = new JPanel();
		p.setSize(500, 500);
		p.setLayout(null);
		f.add(p);

		JTextField t = new JTextField();
		t.setText("12131");
		t.setBounds(0, 0, 300, 20);
		
		JButton b = new JButton();
		b.setText("123");
		b.setBounds(300, 0, 100, 20);
		
		p.add(t);
		p.add(b);
		f.setVisible(true);
	}

}
