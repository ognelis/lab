Проект написан на scala. 
Чтобы запустить проект требуется следующие действия:
  1. Установить Java Platform (JDK) 8u65 :
    * зайти на страницу по сылке: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html;
    * на странице выбрать нужный файл Java SE Development Kit 8u65 под нужную платформу;
    * cкачать, установить.
  2. Установить sbt(Simple Building Tool) : 
       *Для linux:
         * зайти на страницу по сылке: http://www.scala-sbt.org/0.13/tutorial/Installing-sbt-on-Linux.html;
         * выполнить инструкции, приведенные на странице, в зависимости от дистрибутива linux;
       *Для windows:
         * зайти на страницу по сылке: http://www.scala-sbt.org/0.13/tutorial/Installing-sbt-on-Windows.html;
         * скачать файл;
  3. Скачать проект с GitHub
  4. Зайти в консоль:
    * с помощью комманд выбранной платформы, зайти в директорию проекта;
    * внутри проекта вести: sbt;
    * вести run
  5. Приложение запущено!
  
  P.S.// Во время работы приложения, в консоли выводиться информация о том, что происходит. Могут вызываться исключения
  в случае:  некорректный URL файла для обработки;
             файл для сортировки СЛИШКОМ ОГРОМЕН;
             
