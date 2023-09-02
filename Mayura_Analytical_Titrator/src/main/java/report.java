package main.java;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.TabStop.Alignment;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spire.pdf.exporting.xps.schema.Break;
import com.itextpdf.text.FontFactory;

public class report {

	static String parameters = "", remarks = "", company_details = "", headers = "", values = "", trials = "",
			result = "", rsd = "", instrument_id = "", report_name = "";
	static boolean graph = false;

	private static void addFooter(PdfWriter writer) {
		PdfPTable footer = new PdfPTable(1);
		try {
			footer.setWidths(new int[] { 1 });
			footer.setTotalWidth(500);
			footer.setLockedWidth(true);
			footer.getDefaultCell().setFixedHeight(20);
			footer.getDefaultCell().setBorder(0);

			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 0.10f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

			float fntSize, lineSpacing;
			fntSize = 10.0f;
			lineSpacing = 10f;
			footer.addCell(new Phrase("Report generated using model " + instrument_id + " by Mayura Analytical on "
					+ get_date() + "   " + get_time(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize)));

			// write page
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			footer.writeSelectedRows(0, -1, 34, 50, canvas);
			canvas.endMarkedContentSequence();
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	public static PdfPCell createTextCell(String text, Font fontH1) throws DocumentException, IOException {

		PdfPCell cell = new PdfPCell();

		Paragraph p = new Paragraph(text, fontH1);
		p.setAlignment(Element.ALIGN_CENTER);
		cell.addElement(p);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;

	}

	public static PdfPCell createImageCell(Image img2) throws DocumentException, IOException {
		Image img = Image.getInstance(img2);
		img.scaleAbsolute(502f, 420f);
		PdfPCell cell = new PdfPCell(img, true);
		cell.setBorderColor(BaseColor.WHITE);
		return cell;
	}

	public static void generate_report(String company_detail, String parameter, String header1, String value,
			Boolean graphs, String trial, String results, String rsds, String remark1)
			throws FileNotFoundException, DocumentException {

		System.out.println("Generate Report Result = " + results);
		// System.out.println("Generate Report2 = "+value);

		remarks = remark1;
		parameters = parameter;
		company_details = company_detail;
		headers = header1;
		values = value;
		String temp_val = "";
		String[] temp_values = values.split(":");
		for (int i = 0; i < temp_values.length; i++) {
			if (i == 0) {
				temp_val = temp_val + (i + 1) + "," + temp_values[i];
			} else {
				temp_val = temp_val + ":" + (i + 1) + "," + temp_values[i];
			}
		}

		values = temp_val;
		trials = trial;
		result = results;
		rsd = rsds;
		graph = graphs;

		String company[] = company_details.split(">");
		String param[] = parameters.split(",");
		String header[] = headers.split(",");
		instrument_id = param[8];
		report_name = param[13];
		boolean is_standardization = false;

		String trial_data[] = trials.split("-");
		int trials_row = trial_data.length;
		int param_number = param.length;
		//System.out.println("RESULTTTTT UNIT  +++ == "+param[16]);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream("C:\\SQLite\\Report\\" + param[13] + "-" + param[0] + ".pdf"));
		String[] value_data = values.split(":");
		int row_number = value_data.length;
		int col_number = value_data[0].split(",").length;

		System.out.println("row= " + row_number + " col= " + col_number + " pnum = " + param_number);

		try {
			document.open();
			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			

			Image img = Image.getInstance("C:\\sqlite\\company_logo\\logo.png");
			PdfPTable table12 = new PdfPTable(2);
			table12.setWidthPercentage(100);
			table12.setWidths(new int[] { 1, 12 });
			table12.addCell(createImageCell(img));
			
			Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			table12.addCell(createTextCell(company[0], fontH1));
			PdfPCell cell = new PdfPCell();
			
			Paragraph paragraph1 = new Paragraph("");
			cell.addElement(paragraph1);
			cell.setBorderColor(BaseColor.WHITE);
			table12.addCell(cell);
			
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
			table12.addCell(createTextCell(company[1], fontH1));
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			document.add(table12);

			PdfPTable table = new PdfPTable(2);
			Paragraph p = new Paragraph(" ", f);
			addEmptyLine(p, 0);
			document.add(p);

			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
			if(param[0].contains("Standardisation")) {
				Paragraph paragraph21 = new Paragraph("Standardisation Report",fontH1);
				paragraph21.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph21);
				document.add(p);
				is_standardization = true;
			}
			else {
				Paragraph paragraph21 = new Paragraph("Moisture Estimation Report",fontH1);
				paragraph21.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph21);
				document.add(p);
			}

			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			
			table.setWidths(new float[] { 2f, 1f });
			table.getDefaultCell().setBorder(0);

			table.setPaddingTop(5.0f);
			int w = 1;
			for (int l = 1; l < (param_number - 3); l++) {
				table.addCell(param[l]);

				if (w == 2) {
					w = 0;
					table.addCell(" ");
					table.addCell(" ");
				}
				w++;

			}
			document.add(table);
			addEmptyLine(p, 1);
			document.add(p);

			PdfPTable table1 = new PdfPTable(col_number);
			for (int j = 0; j < col_number; j++) {
				PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell1);
			}
			table1.completeRow();

			for (int i = 0; i < row_number; i++) {
				int q = 1;
				String row_value[] = value_data[i].split(",");
				System.out.println(Arrays.toString(row_value));

				for (int h = 0; h < col_number; h++) {
					PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
					cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell01);
				}
				q++;
				table1.completeRow();
			}

			document.add(table1);

			Paragraph p2 = new Paragraph(" ", f1);

			addEmptyLine(p2, 2);
			document.add(p2);

			PdfPTable table3 = new PdfPTable(2);
			table3.getDefaultCell().setBorderColor(BaseColor.WHITE);
			if(is_standardization == true) {
				table3.addCell("Result	: " + result);
				is_standardization = false;
			}
			else {
				table3.addCell("Result	: " + result+" "+param[16]);
			}
			table3.addCell("RSD : " + rsd);
			table3.addCell(" ");
			table3.addCell(" ");
	
			

			document.add(table3);

			Paragraph premarks = new Paragraph("Remarks : " + remarks);
			addEmptyLine(premarks, 1);
			addEmptyLine(premarks, 1);

			premarks.setIndentationLeft(55.0f);
			document.add(premarks);

			if (param[15].matches("Not Certified")) {
				String[] temp = param[10].split(" ");
				PdfPTable table4 = new PdfPTable(2);
				table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
				table4.addCell("\nAnalyzed By ");// +result
				table4.addCell("\nChecked By ");// +rsd
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");

				document.add(table4);
			} else {
				String[] temp = param[10].split(" ");
				PdfPTable table4 = new PdfPTable(2);
				table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
				table4.addCell("\nAnalyzed By ");// +result
				table4.addCell("\nChecked By ");// +rsd
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");

				document.add(table4);

			}

			//
			// Paragraph panalyzed= new Paragraph("ANALYSED BY: CERTIFIED BY: ",f1);
			// addEmptyLine(panalyzed, 2);
			// panalyzed.setIndentationLeft(55.0f);

			// document.add(panalyzed);
			addFooter(writer);

			if (graph) {
				for (int graph_trial = 0; graph_trial < row_number; graph_trial++) {
					document.newPage();
					Image imag = Image.getInstance("C:\\sqlite\\logo\\logo.png");
					imag.scaleAbsolute(500f, 410f);

					document.add(imag);
					Paragraph test = new Paragraph("     ", f1);
					Paragraph test1 = new Paragraph("     ", f1);

					Paragraph test2 = new Paragraph("     ", f1);
					document.add(test);
					document.add(test1);
					document.add(test2);

					PdfPTable table_trial = new PdfPTable(2);
					table_trial.setWidths(new float[] { 2f, 1f });
					table_trial.getDefaultCell().setBorder(0);

					table.setPaddingTop(5.0f);
					int w1 = 1;
					System.out.println(trial_data[0].split(",").length);
					for (int v = 0; v < trial_data[0].split(",").length; v++) {

						table_trial.addCell(trial_data[0].split(",")[v]);

						if (w1 == 2) {
							w1 = 0;
							table_trial.addCell(" ");
							table_trial.addCell(" ");
						}
						w1++;

					}
					document.add(table_trial);

					PdfPTable trial_individual = new PdfPTable(col_number);
					for (int j = 0; j < col_number; j++) {
						PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						trial_individual.addCell(cell1);

					}
					trial_individual.completeRow();

					String row_value[] = value_data[graph_trial].split(",");
					System.out.println(Arrays.toString(row_value));

					for (int h = 0; h < col_number; h++) {
						PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
						cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
						trial_individual.addCell(cell01);
					}
					trial_individual.completeRow();

					document.add(trial_individual);
					// addEmptyLine(premarks, 1);
					// addEmptyLine(premarks, 1);

					document.add(premarks);
					// document.add(panalyzed);
					document.add(test1);
					document.add(test2);
					addFooter(writer);
				}
			}

			document.close();
			writer.close();

			try {
				File file = new File("C:\\SQLite\\Report\\" + report_name + "-" + param[0] + ".pdf");
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generate_report_burette_calib(String company_detail, String parameter, String header1,
			String trials, String analyzed_by, String instrument_id1) throws FileNotFoundException, DocumentException {

		// System.out.println("Generate Report Result = "+results);
		// System.out.println("Generate Report2 = "+value);

		parameters = parameter;
		company_details = company_detail;
		headers = header1;
		String temp_val = "";

		String company[] = company_details.split(">");
		String param[] = parameters.split(",");
		String header[] = headers.split(",");
		instrument_id = instrument_id1;

		int param_number = param.length;
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
				"C:\\SQLite\\Report\\Burette Calibration Report - " + get_date() + "__" + get_time_bc() + ".pdf"));

		String[] value_data = trials.split(":");
		int row_number = value_data.length;
		int col_number = value_data[0].split(",").length;

		System.out.println("row= " + row_number + " col= " + col_number + " pnum = " + param_number);

		try {
			document.open();
			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			

			Image img = Image.getInstance("C:\\sqlite\\company_logo\\logo.png");
			PdfPTable table12 = new PdfPTable(2);
			table12.setWidthPercentage(100);
			table12.setWidths(new int[] { 1, 12 });
			table12.addCell(createImageCell(img));
			Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

			table12.addCell(createTextCell(company[0], fontH1));
			PdfPCell cell = new PdfPCell();
			Paragraph paragraph1 = new Paragraph("");
			cell.addElement(paragraph1);
			cell.setBorderColor(BaseColor.WHITE);
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
			table12.addCell(createTextCell(company[1], fontH1));
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			// table12.addCell(createTextCell(param[0],fontH1));

			document.add(table12);

			PdfPTable table = new PdfPTable(2);

			Paragraph p = new Paragraph(" ", f);
			addEmptyLine(p, 0);
			document.add(p);
			
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
			Paragraph paragraph21 = new Paragraph("Burette Calibration Report",fontH1);
			paragraph21.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph21);
			
			Paragraph p22 = new Paragraph(" ", f);
			addEmptyLine(p22, 0);
			document.add(p22);

			table.setWidths(new float[] { 2f, 1f });
			table.getDefaultCell().setBorder(0);

			table.setPaddingTop(5.0f);
			int w = 1;
			for (int l = 1; l < (param_number); l++) {
				System.out.println("Param = " + param[l]);
				table.addCell(param[l]);

				if (w == 2) {
					w = 0;
					table.addCell(" ");
					table.addCell(" ");
				}
				w++;

			}
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			document.add(table);

			PdfPTable table1 = new PdfPTable(col_number);
			for (int j = 0; j < col_number; j++) {
				PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell1);
			}
			table1.completeRow();

			for (int i = 0; i < row_number; i++) {
				int q = 1;
				String row_value[] = value_data[i].split(",");
				System.out.println(Arrays.toString(row_value));

				for (int h = 0; h < col_number; h++) {
					PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
					cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell01);
				}
				q++;
				table1.completeRow();
			}

			document.add(table1);

			Paragraph p2 = new Paragraph(" ", f1);

			addEmptyLine(p2, 2);
			document.add(p2);
			
			Paragraph paragraph2 = new Paragraph("               W1 - Weight of empty beaker",f2);
			document.add(paragraph2);

			paragraph2 = new Paragraph("               W2 - Weight of beaker with dosed water",f2);
			document.add(paragraph2);
			addEmptyLine(p2, 2);
			document.add(p2);


			
			paragraph2 = new Paragraph("                Burette Factor : "+get_burette_factor(),f2);
			document.add(paragraph2);
			addEmptyLine(p2, 2);
			document.add(p2);
			

			PdfPTable table4 = new PdfPTable(2);
			table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
			table4.addCell("\nAnalyzed By");// +result
			table4.addCell("\nChecked By ");// +rsd
			table4.addCell(" ");
			table4.addCell(" ");
			table4.addCell(" ");
			table4.addCell(" ");

			document.add(table4);

			addFooter(writer);

			document.close();
			writer.close();

			try {
				File file = new File("C:\\SQLite\\Report\\Burette Calibration Report - " + get_date() + "__"
						+ get_time_bc() + ".pdf");
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double get_burette_factor() {
		double temp_bf = 0;
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		String sql;

		sql = "SELECT * FROM config_param WHERE cnfg_param_group = 'buretteFactor' and cnfg_param_name = 'buretteFactor'";

		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			temp_bf = Double.parseDouble(rs.getString("cnfg_param_value"));
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				System.out.println(e1.toString());
			}
		}

		return temp_bf;
	}
	
	
	
	
	public static void generate_report_electrode_calib(String company_detail, String parameter, String header1,
			String trials, String analyzed_by, String instrument_id1) throws FileNotFoundException, DocumentException {

		// System.out.println("Generate Report Result = "+results);
		// System.out.println("Generate Report2 = "+value);

		parameters = parameter;
		company_details = company_detail;
		headers = header1;
		String temp_val = "";

		String company[] = company_details.split(">");
		String param[] = parameters.split(",");
		String header[] = headers.split(",");
		instrument_id = instrument_id1;

		int param_number = param.length;
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
				"C:\\SQLite\\Report\\Electrode Calibration Report - " + get_date() + "__" + get_time_bc() + ".pdf"));

		String[] value_data = trials.split(":");
		int row_number = value_data.length;
		int col_number = value_data[0].split(",").length;

		System.out.println("row= " + row_number + " col= " + col_number + " pnum = " + param_number);

		try {
			document.open();
			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			

			Image img = Image.getInstance("C:\\sqlite\\company_logo\\logo.png");
			PdfPTable table12 = new PdfPTable(2);
			table12.setWidthPercentage(100);
			table12.setWidths(new int[] { 1, 12 });
			table12.addCell(createImageCell(img));
			Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

			table12.addCell(createTextCell(company[0], fontH1));
			PdfPCell cell = new PdfPCell();
			Paragraph paragraph1 = new Paragraph("");
			cell.addElement(paragraph1);
			cell.setBorderColor(BaseColor.WHITE);
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
			table12.addCell(createTextCell(company[1], fontH1));
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			// table12.addCell(createTextCell(param[0],fontH1));

			document.add(table12);

			PdfPTable table = new PdfPTable(2);

			Paragraph p = new Paragraph(" ", f);
			addEmptyLine(p, 0);
			document.add(p);
			
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			Paragraph paragraph21 = new Paragraph("Electrode Calibration Report",fontH1);
			paragraph21.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph21);
			
			Paragraph p22 = new Paragraph(" ", f);
			addEmptyLine(p22, 0);
			document.add(p22);

			table.setWidths(new float[] { 2f, 1f });
			table.getDefaultCell().setBorder(0);

			table.setPaddingTop(5.0f);
			int w = 1;
			for (int l = 1; l < (param_number); l++) {
				System.out.println("Param = " + param[l]);
				table.addCell(param[l]);

				if (w == 2) {
					w = 0;
					table.addCell(" ");
					table.addCell(" ");
				}
				w++;

			}
			document.add(table);
			addEmptyLine(p, 2);
			document.add(p);

			PdfPTable table1 = new PdfPTable(col_number);
			for (int j = 0; j < col_number; j++) {
				PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell1);
			}
			table1.completeRow();

			for (int i = 0; i < row_number; i++) {
				int q = 1;
				String row_value[] = value_data[i].split(",");
				System.out.println(Arrays.toString(row_value));

				for (int h = 0; h < col_number; h++) {
					PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
					cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell01);
				}
				q++;
				table1.completeRow();
			}

			document.add(table1);

			Paragraph p2 = new Paragraph(" ", f1);

			addEmptyLine(p2, 2);
			document.add(p2);
			
			addEmptyLine(p2, 2);
			document.add(p2);
			document.add(p2);


			

			PdfPTable table4 = new PdfPTable(2);
			table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
			table4.addCell("\nAnalyzed By");// +result
			table4.addCell("\nChecked By ");// +rsd
			table4.addCell(" ");
			table4.addCell(" ");
			table4.addCell(" ");
			table4.addCell(" ");

			document.add(table4);

			addFooter(writer);

			document.close();
			writer.close();

			try {
				File file = new File("C:\\SQLite\\Report\\Electrode Calibration Report - " + get_date() + "__"
						+ get_time_bc() + ".pdf");
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generate_method_report(String company_detail, String parameter, String experiment,
			String metd_name, String inst_id) throws FileNotFoundException, DocumentException {
		System.out.println("Company Details: " + company_detail);
		parameters = parameter;

		String temp_val = "";
		String[] temp_values = values.split(":");
		for (int i = 0; i < temp_values.length; i++) {
			if (i == 0) {
				temp_val = temp_val + (i + 1) + "," + temp_values[i];
			} else {
				temp_val = temp_val + ":" + (i + 1) + "," + temp_values[i];
			}
		}

		values = temp_val;

		String company[] = company_detail.split(">");
		String param[] = parameters.split(",");
		instrument_id = inst_id;

		String trial_data[] = trials.split("-");
		int trials_row = trial_data.length;
		int param_number = param.length;
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream("C:\\SQLite\\Report\\" + experiment + "-" + metd_name + "-Method.pdf"));
		String[] value_data = values.split(":");
		int row_number = value_data.length;
		int col_number = value_data[0].split(",").length;

		System.out.println("row= " + row_number + " col= " + col_number + " pnum = " + param_number);

		try {
			document.open();
			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

			Image img = Image.getInstance("C:\\sqlite\\company_logo\\logo.png");

			PdfPTable table12 = new PdfPTable(2);
			table12.setWidthPercentage(100);
			table12.setWidths(new int[] { 1, 12 });
			table12.addCell(createImageCell(img));
			Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

			table12.addCell(createTextCell(company[0], fontH1));
			PdfPCell cell = new PdfPCell();
			Paragraph paragraph1 = new Paragraph("");
			cell.addElement(paragraph1);
			cell.setBorderColor(BaseColor.WHITE);
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
			table12.addCell(createTextCell(company[1], fontH1));
			table12.addCell(cell);

			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			// table12.addCell(createTextCell(param[0],fontH1));

			document.add(table12);

			PdfPTable table = new PdfPTable(2);

			Paragraph p = new Paragraph(" ", f);

			addEmptyLine(p, 0);
			document.add(p);

			table.setWidths(new float[] { 2f, 1f });
			table.getDefaultCell().setBorder(0);

			table.setPaddingTop(5.0f);
			int w = 1;
			for (int l = 0; l < (param_number); l++) {
				table.addCell(param[l]);

				if (w == 2) {
					w = 0;
					table.addCell(" ");
					table.addCell(" ");
				}
				w++;

			}
			document.add(table);
			addEmptyLine(p, 2);
			document.add(p);

			addFooter(writer);

			document.close();
			writer.close();

			try {
				File file = new File("C:\\SQLite\\Report\\" + experiment + "-" + metd_name + "-Method.pdf");
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Check if all the company date is uptodate Eg, Logo");

		}
	}

	public static void generate_report_pot(String company_detail, String parameter, String header1,
			String main_trial_table_data, Boolean graphs, String result_rsd, String remark1, String[] threshold,
			String[] timings, String stir_time, String pre_dose, String metd_file_name, String dose_speed,
			String end_point, String report_name,String inst_id, String formula_no) throws FileNotFoundException, DocumentException {

		System.out.println("Generate Report Result = " + result_rsd);
		// System.out.println("Generate Report2 = "+value);

		remarks = remark1;
		parameters = parameter;
		company_details = company_detail;
		headers = header1;
		result = result_rsd;
		graph = graphs;

		String company[] = company_details.split(">");
		String param[] = parameters.split(",");
		String header[] = headers.split(",");
		instrument_id = inst_id;
		// report_name = param[13];

		int param_number = param.length;
		Document document = new Document();
		PdfWriter writer;
		if (graphs == true)
			writer = PdfWriter.getInstance(document,
					new FileOutputStream("C:\\SQLite\\Report\\" + report_name + "-Graph-" + param[0] + ".pdf"));
		else
			writer = PdfWriter.getInstance(document,
					new FileOutputStream("C:\\SQLite\\Report\\" + report_name + "-" + param[0] + ".pdf"));

		String[] main_trial_table_row_data = main_trial_table_data.split(":");
		int row_number = main_trial_table_row_data.length;
		int col_number = main_trial_table_row_data[0].split(",").length;

		System.out.println("row= " + row_number + " col= " + col_number + " pnum = " + param_number + " trial_data = "
				+ main_trial_table_data + " header = " + headers);

		try {
			document.open();
			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

			Image img = Image.getInstance("C:\\sqlite\\company_logo\\logo.png");

			PdfPTable table12 = new PdfPTable(2);
			table12.setWidthPercentage(100);
			table12.setWidths(new int[] { 1, 12 });
			table12.addCell(createImageCell(img));
			Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

			table12.addCell(createTextCell(company[0], fontH1));
			PdfPCell cell = new PdfPCell();
			Paragraph paragraph1 = new Paragraph("");
			cell.addElement(paragraph1);
			cell.setBorderColor(BaseColor.WHITE);
			table12.addCell(cell);
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
			table12.addCell(createTextCell(company[1], fontH1));
			table12.addCell(cell);
			
			document.add(table12);
			
			Paragraph p2 = new Paragraph(" ", f1);
			document.add(p2);

			PdfPTable table = new PdfPTable(2);
			Paragraph p = new Paragraph(" ", f);

			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
			if(formula_no.matches("1")) {
				Paragraph paragraph21 = new Paragraph("STANDARDISATION",fontH1);
				paragraph21.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph21);
				document.add(p);
			}
			else {
				Paragraph paragraph21 = new Paragraph("ASSAY",fontH1);
				paragraph21.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph21);
				document.add(p);
			}
			
			fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL);
			// table12.addCell(createTextCell(param[0],fontH1));

			table.setWidths(new float[] { 2f, 1f });
			table.getDefaultCell().setBorder(0);

			table.setPaddingTop(5.0f);
			int w = 1;

			// Params Table

			for (int l = 1; l < (param_number - 2); l++) {
				table.addCell(param[l]);
				if (w == 2) {
					w = 0;
					table.addCell(" ");
					table.addCell(" ");
				}
				w++;
			}

			document.add(table);
			document.add(p);

			// header

			PdfPTable table1 = new PdfPTable(col_number);
			for (int j = 0; j < col_number; j++) {
				PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell1);
			}
			table1.completeRow();

			// main_table

			for (int i = 0; i < row_number; i++) {
				int q = 1;
				String row_value[] = main_trial_table_row_data[i].split(",");
				System.out.println(Arrays.toString(row_value));

				for (int h = 0; h < col_number; h++) {
					PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
					cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell01);
				}
				q++;
				table1.completeRow();
			}

			document.add(table1);

			p2 = new Paragraph(" ", f1);

			addEmptyLine(p2, 1);
			document.add(p2);

			PdfPTable table3 = new PdfPTable(2);
			table3.getDefaultCell().setBorderColor(BaseColor.WHITE);

			String[] result_arr = result_rsd.split(":");

			System.out.println("Result_ARRAY SIZE = " + result_arr.length);
			if (result_arr.length == 1) {

				String[] temp_arr0 = result_arr[0].split(",");

				table3.addCell("Result:   " + temp_arr0[0]+" "+param[17]);
				table3.addCell("RSD:   " + temp_arr0[1]+" %");
				table3.addCell(" ");
				table3.addCell(" ");
				table3.addCell(" ");
				table3.addCell(" ");

			} else if (result_arr.length == 2) {
				String[] temp_arr0 = result_arr[0].split(",");
				String[] temp_arr1 = result_arr[1].split(",");

				table3.addCell("Result 1:   " + temp_arr0[0]+" "+param[17]);
				table3.addCell("RSD 1:   " + temp_arr0[1]+" %");
				table3.addCell("Result 2:   " + temp_arr1[0]+" "+param[17]);
				table3.addCell("RSD 2:   " + temp_arr1[1]+" %");
				table3.addCell(" ");
				table3.addCell(" ");

			} else if (result_arr.length == 3) {
				String[] temp_arr0 = result_arr[0].split(",");
				String[] temp_arr1 = result_arr[1].split(",");
				String[] temp_arr2 = result_arr[2].split(",");

				table3.addCell("Result 1:   " + temp_arr0[0]+" "+param[17]);
				table3.addCell("RSD  1:   " + temp_arr0[1]+" %");
				table3.addCell("Result 2:   " + temp_arr1[0]+" "+param[17]);
				table3.addCell("RSD 2:   " + temp_arr1[1]+" %");
				table3.addCell("Result 3:   " + temp_arr2[0]+" "+param[17]);
				table3.addCell("RSD 3:   " + temp_arr2[1]+" %");

			}

			document.add(table3);

			Paragraph premarks = new Paragraph("Remarks : " + remarks);
			addEmptyLine(premarks, 1);

			premarks.setIndentationLeft(55.0f);
			document.add(premarks);

			if (param[16].matches("Not Certified")) {
				String[] temp = param[1].split(" ");
				PdfPTable table4 = new PdfPTable(2);
				table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
				table4.addCell("\nAnalyzed By ");
				table4.addCell("\nChecked By ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				document.add(table4);
			} else {
				String[] temp = param[1].split(" ");
				PdfPTable table4 = new PdfPTable(2);
				table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
				table4.addCell("\nAnalyzed By ");// +result
				table4.addCell("\nChecked By ");// +rsd
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");
				table4.addCell(" ");

				document.add(table4);

			}
//	            if(row_number <=3) {
//	            Paragraph p11xcs = new Paragraph(" ",f1);
//	            	addEmptyLine(p11xcs, 1);
//	            	addEmptyLine(p11xcs, 1);
//	           		addEmptyLine(p11xcs, 1);
//	          		document.add(p11xcs);
//	            }

			addFooter(writer);

			if (graph) {

				for (int graph_trial = 0; graph_trial < row_number; graph_trial++) {
					document.newPage();
					Image imag = Image.getInstance("C:\\sqlite\\chart\\chart" + graph_trial + ".png");
					imag.scaleAbsolute(500f, 280f);

					document.add(imag);
					Paragraph test = new Paragraph("     ", f1);
					Paragraph test1 = new Paragraph("     ", f1);

					Paragraph test2 = new Paragraph("     ", f1);
//			            document.add(test);
//			            document.add(test1);
//			            document.add(test2);

					Paragraph p11 = new Paragraph(" ", f1);

					addEmptyLine(p11, 1);
					document.add(p11);

					PdfPTable table_trial = new PdfPTable(2);
					table_trial.setWidths(new float[] { 2f, 1f });
					table_trial.getDefaultCell().setBorder(0);

					table.setPaddingTop(5.0f);
					int w1 = 1;

					String individual_trial_params = param[3] + ",Time: " + timings[graph_trial] + ",Stir Time: "
							+ stir_time + ",Pre-Dose: " + pre_dose + ",Method File: " + metd_file_name + ",Trial No.: "
							+ (graph_trial + 1) + ",Dosage Speed: " + dose_speed + ",Threshold: "
							+ threshold[graph_trial] + ",End Point: " + end_point + ", Report Name: " + report_name;

					for (int v = 0; v < individual_trial_params.split(",").length; v++) {
						table_trial.addCell(individual_trial_params.split(",")[v]);
						if (w1 == 2) {
							w1 = 0;
							table_trial.addCell(" ");
							table_trial.addCell(" ");
						}
						w1++;
					}
					document.add(table_trial);

					Paragraph p12 = new Paragraph(" ", f1);

					addEmptyLine(p12, 1);
					document.add(p12);

					PdfPTable trial_individual = new PdfPTable(col_number);
					for (int j = 0; j < col_number; j++) {
						PdfPCell cell1 = new PdfPCell(new Phrase(header[j]));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						trial_individual.addCell(cell1);
					}
					trial_individual.completeRow();

					String row_value[] = main_trial_table_row_data[graph_trial].split(",");
					System.out.println(Arrays.toString(row_value));

					for (int h = 0; h < col_number; h++) {
						PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
						cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
						trial_individual.addCell(cell01);
					}
					trial_individual.completeRow();

					document.add(trial_individual);
					Paragraph p4 = new Paragraph(" ", f1);

					addEmptyLine(p4, 1);
					document.add(p4);

					// document.add(premarks);
					// document.add(panalyzed);

					if (param[16].matches("Not Certified")) {
						String[] temp = param[1].split(" ");
						PdfPTable table4 = new PdfPTable(2);
						table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
						table4.addCell("\nAnalyzed By ");// +result
						table4.addCell("\nChecked By ");// +rsd
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						document.add(table4);
					} else {
						String[] temp = param[1].split(" ");
						PdfPTable table4 = new PdfPTable(2);
						table4.getDefaultCell().setBorderColor(BaseColor.WHITE);
						table4.addCell("\nAnalyzed By ");// +result
						table4.addCell("\nChecked By ");// +rsd
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");
						table4.addCell(" ");

						document.add(table4);

					}

					document.add(test1);
					document.add(test2);
					addFooter(writer);
				}
			}

			document.close();
			writer.close();

			try {
				File file;
				if (graphs == true)
					file = new File("C:\\SQLite\\Report\\" + report_name + "-Graph-" + param[0] + ".pdf");
				else
					file = new File("C:\\SQLite\\Report\\" + report_name + "-" + param[0] + ".pdf");

				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public static PdfPCell getCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}

	public static PdfPCell getCell(Image text, int alignment) {
		PdfPCell cell = new PdfPCell(text);
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}

	public static void print_audit_log(String logo_path, String comp_name, String company_address, String instrument_id,
			String data, String from_date, String to_date) throws FileNotFoundException, DocumentException {
		// row1:row2:row3
		// row1 = a,b,c,d,e
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Mayura\\" + comp_name + ".pdf"));
		String[] data_rows = data.split(":");
		int data_row_number = data_rows.length;
		try {
			document.open();

			com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12.0f,
					com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

			Image img = Image.getInstance(logo_path);
			img.scaleAbsolute(50f, 40f);
			// document.add( img);

			PdfPTable table0 = new PdfPTable(3);

			table0.addCell(getCell(img, PdfPCell.ALIGN_LEFT));
			table0.addCell(getCell("Text in the middle", PdfPCell.ALIGN_CENTER));
			table0.addCell(getCell("                  ", PdfPCell.ALIGN_RIGHT));

			document.add(table0);
			PdfPTable table1 = new PdfPTable(3);
			table1.addCell(getCell("             ", PdfPCell.ALIGN_LEFT));
			table1.addCell(getCell(company_address, PdfPCell.ALIGN_CENTER));
			table1.addCell(getCell("                  ", PdfPCell.ALIGN_RIGHT));
			document.add(table1);

			for (int i = 0; i < data_row_number; i++) {
				int q = 1;
				String row_value[] = data_rows[i].split(",");
				System.out.println(Arrays.toString(row_value));

				for (int h = 0; h < 5; h++) {
					PdfPCell cell01 = new PdfPCell(new Phrase(String.valueOf(row_value[h])));
					cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell01);
				}
				q++;
				table1.completeRow();
			}
			document.add(table1);
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get_date() {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}

	public static String get_time() {
		DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss aa");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}

	public static String get_time_bc() {
		DateFormat dateFormat2 = new SimpleDateFormat("hh-mm-ss aa");
		String date_time = dateFormat2.format(new Date()).toString();
		return date_time;
	}
}
