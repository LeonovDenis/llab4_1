<%-- 
    Document   : message
    Created on : 06.04.2020, 23:58:53
    Author     : Анюта
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ввод сообщения</title>
        <style type="text/css">
            html {
                background: url(graphics/fon.jpg) no-repeat center center fixed;
                -webkit-background-size: cover;
                -moz-background-size: cover;
                -o-background-size: cover;
                background-size: cover;
            }
        </style>
    </head>
    <body>
    <center>
        <style>
            section {
                color: black;
                border-radius: 1em;
                padding: 1em;
                position: absolute;
                top: 50%;
                left: 50%;
                margin-right: -50%;
                transform: translate(-50%, -50%) }
            </style>
            <section>
                <table  bgcolor="c0e4ff" border="2"
                        cellspacing="5" >
                    <tr>
                        <td colspan="2">
                    <center>
                        Сообщение вводить тут
                    </center>
                    </td>
                    </tr>
                    <tr>
                        <td >    
                            <form name="textF" action="toServ" method="POST"> 
                                <table  >
                                    <tr>   
                                        <td >
                                            <input type="text" name="message" value="" size="20" required placeholder="Текстовая форма"/>
                                        </td>
                                    </tr>     
                                    <tr>
                                        <td colspan="2">
                                            <style type="text/css">
                                            .bt{width:100%}
                                        </style>
                                        <input  class="bt" name="save_text" width="100" type="submit" value="Отправить"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>  
                    <td >    
                        <form name="numbF" action="toServ" method="POST"> 
                            <table  >
                                <tr>    
                                    <td > 
                                        <input type="text" name="message" value="" size="20" required placeholder="Целочисленная форма"/>
                                    </td>
                                </tr>     
                                <tr>
                                    <td colspan="2">        
                                        <input  class="bt" name="save_numb" width="100" type="submit" value="Отправить"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>  
                </tr> 
                <tr><td colspan="2" align="center">Кнопочный бар</td>
                <form name="buttonsF" action="toServ" method="POST"> 
                    <tr >
                        <td>                     
                            <input  class="bt" name="list" width="100" type="submit" value="Получить список"/>
                        </td>
                        <td>                        
                            <input  class="bt" name="summ" width="100" type="submit" value="Получить сумму цифр"/>
                        </td>
                    </tr>
                    <tr > 
                        <td>                     
                            <button  class="bt" formaction="message.jsp">Очистить</button>
                        </td>
                        <td>                        
                            <input  class="bt" name="return" width="100" type="submit" value="Закрыть сессию"/>
                        </td>
                    </tr>
                </form>
            </table>
            </td>  
            </tr> 
            </table> 


    </center>
    <br><br><br>
    <big>Список</big>
    <style>
        ol {
            color: infotext;
        }
    </style>
    <ol color="white">
        <% for (int i = 0; i < 10; i++) {

                out.println("<li type=\"I\">" + i);
                out.println("</li><BR>");
            }
        %>
    </ol>


</section>



</body>
</html>
