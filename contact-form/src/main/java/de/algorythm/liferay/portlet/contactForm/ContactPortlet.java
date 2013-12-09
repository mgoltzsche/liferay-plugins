package de.algorythm.liferay.portlet.contactForm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class ContactPortlet extends MVCPortlet {

	static private final Log log = LogFactoryUtil.getLog(ContactPortlet.class);
	static private final String FROM = "from";
	static private final String SUBJECT = "subject";
	static private final String MESSAGE = "message";

	/**
	 * Process contact form action
	 */
	@Override
	public void processAction(final ActionRequest request,
			final ActionResponse response) throws IOException, PortletException {
		try {
			checkCaptcha(request);
		} catch (Exception e) {
			log.debug("Invalid captcha", e);
			System.out.println("Invalid captcha");
			e.printStackTrace();
			SessionErrors.add(request, "invalidCaptcha");
		}

		final String from = request.getParameter(FROM);
		final String subject = request.getParameter(SUBJECT);
		final String message = request.getParameter(MESSAGE);

		if (from == null || message == null)
			SessionErrors.add(request, "missingFields");
		
		if (from != null && !Validator.isEmailAddress(from))
			SessionErrors.add(request, "invalidEmail");
		
		final String portletName = getPortletName();
		final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		final long plid = themeDisplay.getLayout().getPlid();
		//final PortletURL redirectURL = PortletURLFactoryUtil.create(request, portletName, plid, PortletRequest.RENDER_PHASE);
		
		//redirectURL.setPortletMode(PortletMode.VIEW);
		//redirectURL.setWindowState(WindowState.NORMAL);
		
		if (SessionErrors.size(request) == 0) {
			sendMail(from, "John Doe", subject, message);
			SessionMessages.add(request, "emailSent");
		} else {
			if (from != null)
				response.setRenderParameter(FROM, from);
			if (subject != null)
				response.setRenderParameter(SUBJECT, subject);
			if (message != null)
				response.setRenderParameter(MESSAGE, message);
		}
		
		//response.sendRedirect(redirectURL.toString());
		super.processAction(request, response);
	}
	
	@Override
	public void doView(final RenderRequest request,
			final RenderResponse response) throws IOException,
			PortletException {
		request.setAttribute(FROM, request.getParameter(FROM));
		request.setAttribute(SUBJECT, request.getParameter(SUBJECT));
		request.setAttribute(MESSAGE, request.getParameter(MESSAGE));
		request.setAttribute("serverName", request.getServerName());
		
		super.doView(request, response);
	}

	/**
	 * Serve captcha image
	 */
	@Override
	public void serveResource(final ResourceRequest resourceRequest,
			final ResourceResponse resourceResponse) throws IOException,
			PortletException {
		try {
			CaptchaUtil.serveImage(resourceRequest, resourceResponse);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void sendMail(final String fromEmail, final String fromName,
			final String subject, final String message)
			throws UnsupportedEncodingException {
		final InternetAddress from = new InternetAddress(fromEmail, fromName);
		final InternetAddress to = new InternetAddress("max.goltzsche@gmail.com", "Max Goltzsche");
		final MailMessage emailMessage = new MailMessage(from, to, subject, message, false);
		
		MailServiceUtil.sendEmail(emailMessage);
		log.info("email sent by " + fromEmail);
	}
	
	private void checkCaptcha(final PortletRequest request) throws Exception {
		final String enteredCaptchaText = request.getParameter("captchaText");
		final PortletSession session = request.getPortletSession();
		final String captchaText = getCaptchaValueFromSession(session);

		if (Validator.isNull(captchaText))
			throw new Exception(
					"Internal Error! Captcha text not found in session");
		
		if (!captchaText.equals(enteredCaptchaText))
			throw new Exception("Invalid captcha text. Please reenter.");
	}

	private String getCaptchaValueFromSession(final PortletSession session) {
		Enumeration<String> atNames = session.getAttributeNames();

		while (atNames.hasMoreElements()) {
			String name = atNames.nextElement();

			if (name.contains("CAPTCHA_TEXT"))
				return (String) session.getAttribute(name);
		}

		return null;
	}
}