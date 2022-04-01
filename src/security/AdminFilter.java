package security;

import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.vfs2.provider.URLFileNameParser;

import beans.LoginBean;
import controllers.Controller;
import entities.User;

@WebFilter(filterName = "AdminFilter", urlPatterns = { "/Admin/*" })
public class AdminFilter implements Filter {

	@Inject
	LoginBean loginBean;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest reqt = (HttpServletRequest) request;

		HttpServletResponse hresponse = (HttpServletResponse) response;

		if (loginBean != null && loginBean.isLoggedIn()) {
			if (!loginBean.getUser().getDiavatmisiUser().equals("1")) {
				hresponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			} else {
				chain.doFilter(request, response);
			}

		} else {
			hresponse.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
