package com.ccil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	private static int count;

	@GET
	@Path("/editForm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response runPython(@DefaultValue("") @QueryParam("data") String data) {
		//data = "{\"firstName\":\"Prithvi\", \"lastName\": \"Prakash\"}";
		System.out.println(data);
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> fieldNameAndValue = gson.fromJson(data, type);
		String agreementType=fieldNameAndValue.get("agreementType");
		System.out.println(fieldNameAndValue);
		String src = "G:/Softwares/webserver/apache-tomcat-8.0.27/apache-tomcat-8.0.27/webapps/EditAgreementForm/EditAgreementForm/"+agreementType+"Src.pdf";
		String test = "G:/Softwares/webserver/apache-tomcat-8.0.27/apache-tomcat-8.0.27/webapps/EditAgreementForm/EditAgreementForm/"+agreementType+".pdf";
		try {
			File target= new File(test);
			File pdf = new File(src);
			_pdfDocument = PDDocument.load(pdf);
			System.out.println(_pdfDocument.getNumberOfPages());
			PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			List<PDField> fields = acroForm.getFields();
			for (PDField pdField : fields) {
				System.out.println(
						pdField.getFullyQualifiedName());
				if (pdField != null) {
					pdField.setValue(fieldNameAndValue.get(pdField.getFullyQualifiedName()));
					pdField.setReadOnly(true);
				}
			}System.out.println(target.exists()+" "+target.getName()+" "+pdf.getAbsolutePath());
			_pdfDocument.save(target);
			_pdfDocument.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("ok").build();
	}
}
