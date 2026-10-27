classDiagram    
  Estudiante "1" -- "1..*" RegistroAsistencia   
  Estudiante "1" -- "0..*" Alerta  
  EvaluadorInasistencia ..> RegistroAsistencia : procesa  
class Estudiante {        
   -Long idEstudiante       
   -String codigo       
   +getPromedio(): double 
   }    
class EvaluadorInasistencia {
    +evaluarAlertaRoja(int, int): boolean      
    +evaluarAlertaAmarilla(double): boolean    }
