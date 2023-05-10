with Ada.Text_IO; use Ada.Text_IO;
with GNAT.Semaphores; use GNAT.Semaphores;

procedure Dinner_Philosophers_3 is
   task type Philosopher is
      entry Start(Id : Integer);
   end Philosopher;

   Forks : array (1..5) of Binary_Semaphore(true, Default_Ceiling);
   Fork_Limiter : Counting_Semaphore(4, Default_Ceiling);

   task body Philosopher is
      Id : Integer;
      Id_Left_Fork, Id_Right_Fork : Integer;
   begin
      accept Start (Id : in Integer) do
         Philosopher.Id := Id;
      end Start;
      
      Id_Left_Fork := Id;
      Id_Right_Fork := Id rem 5 + 1;

      for I in 1..10 loop
         Put_Line("Philosopher " & Id'Img & " thinking " & I'Img & " time");

         Fork_Limiter.Seize;
         Put_Line("Philosopher " & Id'Img & " allowed to take forks");

         Forks(Id_Left_Fork).Seize;
         Put_Line("Philosopher " & Id'Img & " took left fork");

         Forks(Id_Right_Fork).Seize;
         Put_Line("Philosopher " & Id'Img & " took right fork");

         Put_Line("Philosopher " & Id'Img & " eating" & I'Img & " time");

         Forks(Id_Right_Fork).Release;
         Put_Line("Philosopher " & Id'Img & " put right fork");

         Forks(Id_Left_Fork).Release;
         Put_Line("Philosopher " & Id'Img & " put left fork");

         Fork_Limiter.Release;
         Put_Line("Philosopher " & Id'Img & " no longer needs forks");
      end loop;
   end Philosopher;

   Philosophers : array (1..5) of Philosopher;
Begin
   for I in Philosophers'Range loop
      Philosophers(I).Start(I);
   end loop;

end Dinner_Philosophers_3;
