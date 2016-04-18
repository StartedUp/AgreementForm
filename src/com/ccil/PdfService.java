package com.ccil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
import com.google.gson.reflect.TypeToken;

@Path("/PdfService")
public class PdfService {
	private static PDDocument _pdfDocument;

	@GET
	@Path("/editForm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response runPython(@DefaultValue("") @QueryParam("data") String data,@DefaultValue("") @QueryParam("agreementType") String agreementType) {
		data = "{\"firstName\":\"Prithvi\", \"lastName\": \"Prakash\"}";
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> fieldNameAndValue = gson.fromJson(data, type);
		System.out.println(fieldNameAndValue);
		String src = "G:/my_db/MyGit/StartedUp/AgreementForm/"+agreementType+"Src.pdf";
		String test = "G:/my_db/MyGit/StartedUp/AgreementForm/"+agreementType+".pdf";
		try {
			File pdf = new File(src);
			_pdfDocument = PDDocument.load(pdf);
			System.out.println(_pdfDocument.getNumberOfPages());
			PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			List<PDField> fields = acroForm.getFields();
			for (PDField pdField : fields) {
				System.out.println(
						pdField.getFullyQualifiedName() + " " + fieldNameAndValue.get(pdField.getFullyQualifiedName()));
				if (pdField != null) {
					pdField.setValue(fieldNameAndValue.get(pdField.getFullyQualifiedName()));
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
