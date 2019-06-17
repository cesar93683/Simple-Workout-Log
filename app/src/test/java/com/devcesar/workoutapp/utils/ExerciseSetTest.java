package com.devcesar.workoutapp.utils;

import com.google.common.truth.Truth;
import org.junit.Test;

public class ExerciseSetTest {

  @Test
  public void shouldPrintRepSingular() {
    ExerciseSet exerciseSet = new ExerciseSet(1, 1, 1);
    Truth.assertThat(exerciseSet.toString()).isEqualTo("Set 1 - 1 Rep @ 1 LB");
  }

  @Test
  public void shouldPrintRepsPlural() {
    ExerciseSet exerciseSet = new ExerciseSet(2, 1, 1);
    Truth.assertThat(exerciseSet.toString()).isEqualTo("Set 1 - 2 Reps @ 1 LB");
  }

  @Test
  public void shouldPrintDashForZeroWeight() {
    ExerciseSet exerciseSet = new ExerciseSet(1, 0, 1);
    Truth.assertThat(exerciseSet.toString()).isEqualTo("Set 1 - 1 Rep @ - LB");
  }
}