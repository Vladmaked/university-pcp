with Ada.Text_IO;               use Ada.Text_IO;
with Ada.Numerics.Float_Random; use Ada.Numerics.Float_Random;

procedure Main is
   Num_Of_Threads : Integer := 200;
   Can_Stop       : Boolean := False;
   pragma Atomic (Can_Stop);

   task type Break_Thread;
   task type Main_Thread;

   task body Break_Thread is
   begin
      delay 30.0;
      Can_Stop := True;
   end Break_Thread;

   task body Main_Thread is
      Sum     : Long_Long_Integer := 0;
      Step    : Long_Long_Integer := 3;
      Gen     : Generator;
      task_id : Integer;
   begin

      loop
         begin
            Sum := Sum + Step;
            Reset (Gen);
            task_id := Integer (Random (Gen) * 1_000_000.0);
         end;
         exit when Can_Stop;
      end loop;

      Put_Line
        ("In thread" & Integer'Image (task_id) & " - the sum" &
         Long_Long_Integer'Image (Sum / Step) & " of addends equals" &
         Long_Long_Integer'Image (Sum));
   end Main_Thread;

   B : Break_Thread;
   T : array (1 .. Num_Of_Threads) of Main_Thread;

begin
   null;
end Main;
