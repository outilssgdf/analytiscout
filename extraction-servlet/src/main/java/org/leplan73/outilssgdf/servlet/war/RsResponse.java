package org.leplan73.outilssgdf.servlet.war;

public class RsResponse 
{
	/** 200 (*) Or any other 2xx code */
	public final static int ST_OK = 200;
	public final static String ST_OK_TXT = "OK - The requested content is provided";
	
	/** 201 */
	public final static int ST_CREATED = 201;
	public final static String ST_CREATED_TXT = "Created - The provided item(s) was(were) created";

	/** 204 */
	public final static int ST_NO_CONTENT = 204;
	public final static String ST_NO_CONTENT_TXT = "No Content - Successful operation, but no content is provided";
	
	/** 206 */
	public final static int ST_PARTIAL_CONTENT = 206;
	public final static String ST_PARTIAL_CONTENT_TXT = "Partial Content - Successful operation, but partial content is provided";
	
	/** 400 */
	public final static int ST_BAD_REQUEST = 400;
	public final static String ST_BAD_REQUEST_TXT = "Bad Request - Some request parameters are missing or not valid";
	
	/** 401 (*) */
	public final static int ST_UNAUTHORIZED = 401;
	public final static String ST_UNAUTHORIZED_TXT = "Unauthorized - Authentication has failed or has not yet been provided";

	/** 403 (*) In case of permission checking only */
	public final static int ST_FORBIDDEN = 403;
	public final static String ST_FORBIDDEN_TXT = "Forbidden - Insufficient permissions to use this operation";

	/** 404 */
	public final static int ST_NOT_FOUND = 404;
	public final static String ST_NOT_FOUND_TXT = "Not Found - The requested item was not found";

	/** 416 (*) In case of range management only */
	public final static int ST_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public final static String ST_REQUESTED_RANGE_NOT_SATISFIABLE_TXT = "Requested Range Not Satisfiable - The requested item range cannot be provided";

	/** 500 (*) */
	public final static int ST_INTERNAL_SERVER_ERROR = 500;
	public final static String ST_INTERNAL_SERVER_ERROR_TXT = "Internal Server Error - An unexpected server side error has occurred";
	
	/** 501 */
	public final static int ST_NOT_IMPLEMENTED = 501;
	public final static String ST_NOT_IMPLEMENTED_TXT = "Not Implemented - This operation is not yet implemented for the requested context";

	/** 503 */
	public final static int ST_SERVICE_UNAVAILABLE = 503;
	public final static String ST_SERVICE_UNAVAILABLE_TXT = "Service Unavailable - A back-end service is temporarily unavailable";

	/** 504 */
	public final static int ST_GATEWAY_TIMEOUT = 504;
	public final static String ST_GATEWAY_TIMEOUT_TXT = "Gateway Timeout - Cannot receive a timely response from a back-end service";
}
