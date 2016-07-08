
package de.fhws.sdk.orca.network.asynctask.link.delete;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
import de.fhws.sdk.orca.helper.Constants;
import de.fhws.sdk.orca.helper.NetworkHelper;
import de.fhws.sdk.orca.model.Entity;
import de.fhws.sdk.orca.model.Link;
import de.fhws.sdk.orca.network.callback.INoReturnValueCallback;

/**
 * AsyncTaskKlasse: Dieser Endpunkt dient dazu alle Links von einem Typ zu
 * l�schen. BACKEND-ENDPUNKT: api/{appname}/entities/{id}/links/{type}
 * 
 * @author ThomasDeinlein
 * @param <T> Klasse, die von {@linkplain de.fhws.sdk.orca.model.Entity Entity}
 *            ableitet
 */
public class DeleteAllLinksOfSameType<T extends Entity> extends
		AsyncTask<Void, Void, Void> {
	
	private String					errorMessage	= Constants.NO_ERROR;
	private String					appName;
	private String					apiKey;
	private int						entityId;
	private T						entity;
	private String					linkType;
	private int						statusCodeHttpDelete;
	private HttpClient				httpDeleteClient;
	private HttpDelete				httpDelete;
	private HttpResponse			httpDeleteResponse;
	private INoReturnValueCallback	callback;
	private String					deleteUrl;
	
	public DeleteAllLinksOfSameType(int entityId, String linkType, T entity,
			String appName, String apiKey, INoReturnValueCallback callback) {
	
		this.entityId = entityId;
		this.linkType = linkType;
		this.entity = entity;
		this.callback = callback;
		this.appName = appName;
		this.apiKey = apiKey;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
	
		try {
			if (checkParameters()) {
				
				setDeleteURL();
				setHttpDeleteVariabelsAndHeadersAndExecuteRequest();
				
				if (NetworkHelper.isStatusCode204(statusCodeHttpDelete)) {
					
					formatEntity();
					
				}
				else if (NetworkHelper
						.isStatusCode403Or404(statusCodeHttpDelete)) {
					errorMessage = EntityUtils.toString(
							httpDeleteResponse.getEntity(),
							Constants.UTF_STRING);
				}
				else {
					statusCodeHttpDelete = -1;
					errorMessage = "StatusCode is " + statusCodeHttpDelete
							+ ".";
				}
			}
			else {
				statusCodeHttpDelete = -1;
				errorMessage = "DELETE-Request not possible: ivalid entityId, linkType, appName or apiKey (id==0, null or empty)  ";
			}
		}
		catch (IOException e) {
			Log.e(Constants.TAG_ORCASDK, "IOException " + e.getMessage());
			e.printStackTrace();
			errorMessage = "IOException " + e.getMessage();
			statusCodeHttpDelete = -1;
		}
		catch (NullPointerException e) {
			Log.e(Constants.TAG_ORCASDK,
					"NullPointerException " + e.getMessage());
			e.printStackTrace();
			errorMessage = "NullPointerException " + e.getMessage();
			statusCodeHttpDelete = -1;
		}
		catch (Exception e) {
			Log.e(Constants.TAG_ORCASDK, "Exception " + e.getMessage());
			e.printStackTrace();
			errorMessage = "Exception " + e.getMessage();
			statusCodeHttpDelete = -1;
		}
		return null;
	}
	
	private void formatEntity() {
	
		ArrayList<Link> temp = new ArrayList<Link>();
		
		for (Link link : entity.getLinks()) {
			if (link.getObject().contains(linkType)) {
				
			}
			else {
				temp.add(link);
			}
		}
		
		entity.setLinks(temp);
	}
	
	private void setDeleteURL() throws UnsupportedEncodingException {
	
		deleteUrl = Constants.BACKEND_URI
				+ URLEncoder.encode(appName, Constants.UTF_STRING) + "/"
				+ Constants.ENTITIES + "/" + entityId + "/" + Constants.LINKS
				+ "/" + URLEncoder.encode(linkType, Constants.UTF_STRING);
	}
	
	private void setHttpDeleteVariabelsAndHeadersAndExecuteRequest()
			throws IOException, ClientProtocolException {
	
		httpDeleteClient = new DefaultHttpClient();
		httpDelete = new HttpDelete(deleteUrl);
		httpDelete.addHeader(Constants.APIKEYHEADER, apiKey);
		httpDeleteResponse = httpDeleteClient.execute(httpDelete);
		statusCodeHttpDelete = httpDeleteResponse.getStatusLine()
				.getStatusCode();
	}
	
	private boolean checkParameters() {
	
		return entityId > 0 && linkType != null && !linkType.isEmpty()
				&& appName != null && !appName.isEmpty() && apiKey != null
				&& !apiKey.isEmpty();
	}
	
	@Override
	protected void onPostExecute(Void result) {
	
		if (callback != null) {
			
			callback.onComplete(statusCodeHttpDelete, errorMessage);
		}
	}
}
