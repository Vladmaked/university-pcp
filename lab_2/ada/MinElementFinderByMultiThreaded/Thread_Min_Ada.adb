with Ada.Text_IO; use Ada.Text_IO;

procedure thread_min_ada is

   dim : constant integer := 2000000;
   thread_num : constant integer := 8;

   arr : array(1..dim) of integer;

   procedure Init_Arr is
   begin
      for i in 1..dim loop
         arr(i) := i;
      end loop;
      arr(dim / 2) := -13;
   end Init_Arr;

    type Min_Result is record
      index : Integer;
      value : Integer;
   end record;

   function part_min(start_index, end_index : in integer) return Min_Result is
      min_res : Min_Result := (start_index, arr(start_index));
   begin
      for i in start_index + 1 .. end_index loop
         if arr(i) < min_res.value then
            min_res := (i, arr(i));
         end if;
      end loop;
      return min_res;
   end part_min;

   task type starter_thread is
      entry start(start_index, end_index : in Integer);
   end starter_thread;

   protected part_manager is
      procedure set_part_min(min_val : in Integer);
      entry get_min(min_val : out Integer);
   private
      tasks_count : Integer := 0;
      min1 : Integer := Integer'Last;
   end part_manager;

   protected body part_manager is
      procedure set_part_min(min_val : in Integer) is
      begin
         if min_val < min1 then
            min1 := min_val;
         end if;
         tasks_count := tasks_count + 1;
      end set_part_min;

      entry get_min(min_val : out Integer) when tasks_count = thread_num is
      begin
         min_val := min1;
      end get_min;

   end part_manager;

   task body starter_thread is
      min_res : Min_Result;
      start_index, end_index : Integer;
   begin
      accept start(start_index, end_index : in Integer) do
         starter_thread.start_index := start_index;
         starter_thread.end_index := end_index;
      end start;
      min_res := part_min(start_index  => start_index,
                          end_index => end_index);
      part_manager.set_part_min(min_res.value);
   end starter_thread;

   function parallel_min return Min_Result is
      min_res : Min_Result;
      thread : array(1..thread_num) of starter_thread;
      chunk : constant Integer := dim / thread_num;
   begin
      for i in 1 .. thread_num loop
         thread(i).start((i - 1) * chunk + 1, i * chunk);
      end loop;
      part_manager.get_min(min_res.value);
      for i in 1 .. dim loop
         if arr(i) = min_res.value then
            min_res.index := i;
            exit;
         end if;
      end loop;
      return min_res;
   end parallel_min;

begin
   Init_Arr;
   declare
      min_res : Min_Result := parallel_min;
   begin
      Put_Line("Мінімальне значення: " & min_res.value'img & " з індексом " & min_res.index'img);
   end;
end thread_min_ada;
