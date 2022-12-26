package kh.s13.palette.mypage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kh.s13.palette.member.model.vo.MemberVo;
import kh.s13.palette.review.model.service.ReviewImageService;
import kh.s13.palette.review.model.service.ReviewService;
import kh.s13.palette.review.model.vo.MyReviewVo;
import kh.s13.palette.review.model.vo.ReviewImageVo;
import kh.s13.palette.review.model.vo.ReviewVo;

/**
 * Servlet implementation class MypageWishController
 */
@WebServlet("/mypage_review")
public class MypageReviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MypageReviewController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 내가 작성한 후기 목록
		
		ReviewService service = new ReviewService();
		
		// <내가 정하는 고정개수>
		final int pageSize = 4; // 페이지당 글 수
		
		// <DB에서 불러와야하는 수>
		int cnt = 0; // 총 글 수
		int pageCnt = 0; // 총 페이지 수
		int currentPage = 1; // 현재페이지. 기본 1. 눌리면 바뀜
				
		try {
			String mid = ((MemberVo)request.getSession().getAttribute("loginSsInfo")).getMid();

			cnt = service.selectTotalCnt(mid); // 나의 후기 총 개수
			
			if(cnt < 1) { // 게시글 없는 경우. -> 아래 게시글 selectList 할 필요 없음.
				return;
			}
			try {
				currentPage = Integer.parseInt(request.getParameter("pagenum"));
			} catch (Exception e) {
			}
			pageCnt = (cnt/pageSize) + (cnt%pageSize==0 ? 0 : 1); // 총 페이지 수

			int startRnum = (currentPage - 1) * pageSize + 1; // 해당 페이지의 시작 글 번호
			int endRnum = startRnum + pageSize - 1; // 해당 페이지의 마지막 글 번호
			if(endRnum > cnt ) {
				endRnum = cnt;
			}

			
			// 해당되는 rno의 reviewImage만 가져오게하는 작업은 ReviewService에서
			
			List<MyReviewVo> volist = service.selectMyList(mid, startRnum, endRnum);
			request.setAttribute("reviewlist", volist);
			
		} finally { // finally문은 무조건 거치게됨

			request.setAttribute("pageCnt", pageCnt);
			request.setAttribute("currentPage", currentPage);
			
			String viewPath = "/WEB-INF/view/mypage_review.jsp";
			request.getRequestDispatcher(viewPath).forward(request, response);
		}
					
	}

}
