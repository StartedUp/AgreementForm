package com.ccil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.google.gson.Gson;

@Path("/PdfService")
public class PdfService {
	private static PDDocument _pdfDocument;

	@GET
	@Path("/editForm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response runPython(@DefaultValue("") @QueryParam("data") String data) {
		data = "{\"firstName\":\"Prithvi\", \"lastName\": \"Prakash\"}";
		Gson gson = new Gson();
		LeaseForm form = gson.fromJson(data, LeaseForm.class);
		System.out.println(form);
		String src = "G:/my_db/MyGit/StartedUp/AgreementForm/src.pdf";
		String test = "G:/my_db/MyGit/StartedUp/AgreementForm/test.pdf";
		try {
			File pdf = new File(src);
			_pdfDocument = PDDocument.load(pdf);
			System.out.println(_pdfDocument.getNumberOfPages());
			PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			List<PDField> fields = acroForm.getFields();
			for (PDField pdField : fields) {
				System.out.println(pdField.getFullyQualifiedName()+" "+form.getFirstName());
				if (pdField != null) {
					pdField.setValue(form.getFirstName());
					pdField.setReadOnly(true);
				}
			}
			_pdfDocument.save(test);
			_pdfDocument.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("ok").build();
	}
}
