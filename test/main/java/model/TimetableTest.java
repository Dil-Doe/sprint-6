package main.java.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(session);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession thursdayAdult = new TrainingSession(
                groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)
        );

        TrainingSession mondayChild = new TrainingSession(
                groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        TrainingSession thursdayChild = new TrainingSession(
                groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)
        );

        TrainingSession saturdayChild = new TrainingSession(
                groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)
        );

        timetable.addNewTrainingSession(thursdayAdult);
        timetable.addNewTrainingSession(mondayChild);
        timetable.addNewTrainingSession(thursdayChild);
        timetable.addNewTrainingSession(saturdayChild);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertEquals(2, thursday.size());
        assertTrue(tuesday.isEmpty());

        assertEquals(13, thursday.get(0).getTimeOfDay().getHours());
        assertEquals(20, thursday.get(1).getTimeOfDay().getHours());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(session);

        List<TrainingSession> result1 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        List<TrainingSession> result2 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        assertEquals(1, result1.size());
        assertTrue(result2.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeCombined() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0))
        );

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, result.size());
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByNonExistingTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0))
        );

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        assertTrue(result.isEmpty());
    }

    @Test
    void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Петров", "Пётр", "Петрович");

        Group g1 = new Group("Группа 1", Age.ADULT, 60);
        Group g2 = new Group("Группа 2", Age.ADULT, 60);

        TimeOfDay time = new TimeOfDay(15, 0);

        TrainingSession s1 = new TrainingSession(g1, coach, DayOfWeek.FRIDAY, time);
        TrainingSession s2 = new TrainingSession(g2, coach, DayOfWeek.FRIDAY, time);

        timetable.addNewTrainingSession(s1);
        timetable.addNewTrainingSession(s2);

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, time);

        assertEquals(2, result.size());
        assertTrue(result.contains(s1));
        assertTrue(result.contains(s2));
    }

    @Test
    void testSingleCoachMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0))
        );
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.TUESDAY, new TimeOfDay(11, 0))
        );
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0))
        );

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getCount());
        assertEquals(coach, result.get(0).getCoach());
    }

    @Test
    void testMultipleCoachesSorting() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());

        assertEquals(coach1, result.get(0).getCoach());
        assertEquals(3, result.get(0).getCount());

        assertEquals(coach2, result.get(1).getCoach());
        assertEquals(1, result.get(1).getCount());
    }

    @Test
    void testCoachesWithEqualCount() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0))
        );
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0))
        );

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(11, 0))
        );
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(11, 0))
        );

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getCount());
        assertEquals(2, result.get(1).getCount());
    }
}