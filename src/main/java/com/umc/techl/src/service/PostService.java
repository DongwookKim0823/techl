package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.post.GetPostListRes;
import com.umc.techl.src.model.post.PostContents;
import com.umc.techl.src.model.post.PostNewPostReq;
import com.umc.techl.src.model.post.PostNewPostRes;
import com.umc.techl.src.repository.PostRepository;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.techl.config.BaseResponseStatus.INVALID_JWT;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtService jwtService;
    private final PostRepository postRepository;

    public GetPostListRes getPostListInfo(int bookIdx) throws BaseException {
        try {
            GetPostListRes getPostListRes = postRepository.getPostListInfo(bookIdx);
            return getPostListRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetBookInfoRes getBookInfo(int bookIdx) throws BaseException {
        try {
            GetBookInfoRes bookInfoRes = postRepository.getBookInfoRes(bookIdx);
            return bookInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostNewPostRes createPostContents(int bookIdx, PostNewPostReq contents) throws BaseException {
        try {
            jwtService.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(INVALID_JWT);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            PostContents postContents = new PostContents(bookIdx, userIdx, contents.getType(), contents.getTitle(), contents.getContent(),
                    contents.getContentsImage(), contents.getCoverImage(), contents.getConfirmMethod(), contents.getStartDate(), contents.getEndDate());
            int postIdx = postRepository.createPostContents(postContents);
            PostNewPostRes postNewPostRes = new PostNewPostRes(postIdx);
            return postNewPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}