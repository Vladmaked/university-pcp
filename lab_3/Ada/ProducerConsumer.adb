with Ada.Text_IO, GNAT.Semaphores;
use Ada.Text_IO, GNAT.Semaphores;

with Ada.Containers.Indefinite_Doubly_Linked_Lists;
use Ada.Containers;

procedure Producer_Consumer is
   package String_Lists is new Indefinite_Doubly_Linked_Lists (String);
   use String_Lists;

   Storage_Size   : constant Integer := 3;
   Item_Numbers   : constant Integer := 10;
   Producer_Count : constant Integer := 2;
   Consumer_Count : constant Integer := 3;

   Storage : List;

   Access_Storage : Counting_Semaphore (1, Default_Ceiling);
   Full_Storage   : Counting_Semaphore (Storage_Size, Default_Ceiling);
   Empty_Storage  : Counting_Semaphore (0, Default_Ceiling);

   task type Producer_Task is
      entry Set_Id (Id : Integer);
   end Producer_Task;

   task type Consumer_Task is
      entry Set_Id (Id : Integer);
   end Consumer_Task;

   task body Producer_Task is
      Id : Integer := -1;
   begin
      accept Set_Id (Id : Integer) do
         Producer_Task.Id := Id;
      end Set_Id;

      for i in 1 .. Item_Numbers loop
         Full_Storage.Seize;
         Access_Storage.Seize;

         Storage.Append ("item " & i'Img);
         Put_Line ("Producer " & Id'Img & " added item " & i'Img);

         Access_Storage.Release;
         Empty_Storage.Release;
         delay 1.5;
      end loop;

   end Producer_Task;

   task body Consumer_Task is
      Id : Integer := -1;
   begin
      accept Set_Id (Id : Integer) do
         Consumer_Task.Id := Id;
      end Set_Id;

      for i in 1 .. Item_Numbers loop
         Empty_Storage.Seize;
         Access_Storage.Seize;

         declare
            item : String := First_Element (Storage);
         begin
            Put_Line ("Consumer " & Id'Img & " took " & item);
         end;

         Storage.Delete_First;

         Access_Storage.Release;
         Full_Storage.Release;

         delay 2.0;
      end loop;

   end Consumer_Task;

   Producers : array (1 .. Producer_Count) of Producer_Task;
   Consumers : array (1 .. Consumer_Count) of Consumer_Task;

begin
   for i in 1 .. Producer_Count loop
      Producers (i).Set_Id (i);
   end loop;

   for i in 1 .. Consumer_Count loop
      Consumers (i).Set_Id (i);
   end loop;
end Producer_Consumer;
