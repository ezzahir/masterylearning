package org.masterylearning.service;

import org.masterylearning.domain.Course;
import org.masterylearning.domain.CourseHistory;
import org.masterylearning.domain.Entry;
import org.masterylearning.domain.EntryHistory;
import org.masterylearning.domain.User;
import org.masterylearning.repository.CourseHistoryRepository;
import org.masterylearning.repository.EntryHistoryRepository;
import org.masterylearning.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * The History service is responsible to handle the history related resources for a given user.
 */
@Service
public class HistoryService {

    @Inject CourseHistoryRepository courseHistoryRepository;
    @Inject EntryHistoryRepository entryHistoryRepository;
    @Inject UserRepository userRepository;

    @Inject PlatformTransactionManager transactionManager;

    @Transactional
    public List<CourseHistory> addActiveCourses (List<Course> all) {
        Object principal = SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();

        List<CourseHistory> courseHistoryList = null;

        if (principal instanceof User) {
            Long userId = ((User) principal).id;

            User user = userRepository.getOne (userId);

            courseHistoryList = user.getCourseHistoryList ();
            for (Course course : all) {
                boolean found = false;
                for (CourseHistory courseHistory : courseHistoryList) {
                    if (courseHistory.course.id.equals (course.id)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    CourseHistory courseHistory = new CourseHistory ();
                    courseHistory.course = course;

                    Optional<Entry> first = course.getChildren ().stream ().findFirst ();
                    Entry entry = first.isPresent () ? first.get () : null;

                    courseHistory.lastEntry = entry;

                    courseHistoryList.add (courseHistory);
                    courseHistoryRepository.save (courseHistoryList);

                    EntryHistory entryHistory = new EntryHistory ();
                    entryHistory.course = course;
                    entryHistory.entry = entry;

                    List<EntryHistory> entryHistoryList = courseHistory.getEntryHistoryList ();
                    entryHistoryList.add (entryHistory);

                    entryHistoryRepository.save (entryHistoryList);
                }
            }
        }

        return courseHistoryList;
    }

    @Transactional
    public CourseHistory getCourseHistory (Long courseId) {

        Object principal = SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();

        Long userId = null;
        if (principal instanceof User) {

            User user = (User) principal;
            userId = user.id;
        } else {
            return null;
        }

        User user = userRepository.findOne (userId);

        Optional<CourseHistory> first
                = user.getCourseHistoryList ()
                      .stream ()
                      .filter (courseHistory -> courseHistory.id.equals (courseId))
                      .findFirst ();
            /*CourseHistory courseHistoryFound = null;
            List<CourseHistory> courseHistoryList = user.getCourseHistoryList ();
            for (CourseHistory courseHistory : courseHistoryList) {
                if (courseHistory.id.equals (courseId)) {
                    courseHistoryFound = courseHistory;
                }
            }*/

        if (first.isPresent ()) {
            return first.get ();
        }

            //return courseHistoryFound;


        return null;
    }

}