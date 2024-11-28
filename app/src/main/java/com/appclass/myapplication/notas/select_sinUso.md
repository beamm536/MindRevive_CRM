

no voy a meter el select con el genero, es algo complicado, pero lo dejo por aqui :)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownGenero(){

    var listaGeneros =  listOf("Masculino", "Femenino", "Otro")
    var opcionSeleccionada by remember { mutableStateOf(listaGeneros[0]) }
    var isExpanded by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded}
        ) {
            OutlinedTextField(
                label = { Text("Género") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields
                ),
                modifier = Modifier
                    .menuAnchor(), //esto es muy necesario jajaja hace q salgan las opciones del menu jijiji :)
                value = opcionSeleccionada,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                listaGeneros.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            opcionSeleccionada = listaGeneros[index]
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

            //Text(text = "Género:")
        }
    }
}