package com.megait.artrade.like;

import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final WorkRepository workRepository;

    public Like addLike(Member member, Work work) {

        Like like = likeRepository.findAllByMemberAndWork(member, work);

        if (like == null) {
            Like bulidLike = Like.builder()
                    .status(true)
                    .work(work)
                    .member(member)
                    .likedAt(LocalDateTime.now())
                    .build();
            Like newlike = likeRepository.save(bulidLike);

            Work work_ = workRepository.getById(work.getId());

            work_.setPopularity(work_.getPopularity() + 1);
            workRepository.save(work_);
            return newlike;
        } else {
            boolean mod = like.isStatus();
            if (mod) {
                work.setPopularity(work.getPopularity() - 1);

                workRepository.save(work);
                like.setModifyAt(LocalDateTime.now());
                like.setStatus(false);
                likeRepository.save(like);
            } else {
                work.setPopularity(work.getPopularity() + 1);
                workRepository.save(work);
                like.setModifyAt(LocalDateTime.now());
                like.setStatus(true);
                likeRepository.save(like);
            }
            return like;
        }
    }
}
