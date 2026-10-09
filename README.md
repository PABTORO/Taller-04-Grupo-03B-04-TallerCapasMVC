
## Cómo ejecutar

1. Abre esta carpeta (`04-TallerCapasMVC`) en VS Code con el `Extension Pack for Java` instalado.
2. Abre `src/presentation/GUIQuestions.java`.
3. Dale ▶ Run. Se abren 3 ventanas: la principal (gestión de preguntas) y las dos vistas
   observadoras (estadísticas y gráfica de pastel).

O desde la terminal con:
javac -d bin src/domain/*.java src/access/*.java src/infra/*.java src/presentation/*.java
java -cp bin presentation.GUIQuestions
