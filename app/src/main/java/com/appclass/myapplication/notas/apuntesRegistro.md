
## Campo de la contraseña 

--> tenemos q utilizar una variable q comience en false, para q no se muestre, y
tenemos q tener dos distintas para cada campo de la contraseña, para q no se muestren los dos a la vez

##### CON LA SIGUIENTE LINEA HACEMOS LA TRANSOFORMACION PARA OCULTAR LA CONTRASEÑA CON PUNTITOS
**visualTransformation = PasswordVisualTransformation()**

**trailingIon**
- es una propiedad del outlinedtextfield para poner iconos pero al final del input/campo:)


## Componente Toast

1. creamos una variable q va a tener el contexto en el q se encuntra la aplicacion, es decir cual 
de todas las partes de la aplicacion se está ejecutando  ==> asi es como sabe *COMO* y *CUANDO*
mostrar el TOAST

2. el metodo por defecto del componente, en este caso lo he puesto en la parte dnd ya le he puesto
otras condiciones q tenga q cumplir el usuario (ya tiene los requisitos, el de la contraseña y 
todos los campos), después de eso metemos el metodo propio ---> q se va a encagar de **crear el toast**

3. **PARÁMETROS necesarios para la funcion:**
   3.1. **contexto** --> la variable instanciada al principio
   3.2. **texto** --> entre "" pondremos el texto q queremos q se nos muestre en el toast
   3.3. **duración del componente** --> le indicamos q va a tener un ciclo de vida de tan solo
   unos segundos

4. **show()** --> llama al metodo para mostrar el componente toast
