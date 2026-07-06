with open("app/src/main/java/com/example/ui/PowerLogApp.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for i, line in enumerate(lines):
    if "Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween)" in line:
        new_lines.append(line)
        new_lines.append("                val context = LocalContext.current\n")
        new_lines.append("                val currentProject = viewModel.allProjects.collectAsState().value.find { it.id == viewModel.currentProjectId.value }\n")
        new_lines.append("                Column { \n")
        new_lines.append("                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {\n")
        new_lines.append("                        Button(onClick = { currentProject?.let { ExportManager.exportToExcel(context, it, activities) } }) {\n")
        new_lines.append("                            Icon(Icons.Default.FileDownload, contentDescription = null)\n")
        new_lines.append("                            Spacer(modifier = Modifier.width(4.dp))\n")
        new_lines.append("                            Text(\"Export Excel\")\n")
        new_lines.append("                        }\n")
        new_lines.append("                        Button(onClick = { currentProject?.let { ExportManager.exportToPdf(context, it, activities) } }) {\n")
        new_lines.append("                            Icon(Icons.Default.FileDownload, contentDescription = null)\n")
        new_lines.append("                            Spacer(modifier = Modifier.width(4.dp))\n")
        new_lines.append("                            Text(\"Export PDF\")\n")
        new_lines.append("                        }\n")
        new_lines.append("                    }\n")
        new_lines.append("                    Spacer(modifier = Modifier.height(8.dp))\n")
        new_lines.append("                    currentProject?.let { Iec60034ReportButton(context, it, activities) }\n")
        new_lines.append("                }\n")
        skip = True
    elif skip and "Text(\"Activities\", modifier = Modifier.padding(horizontal = 16.dp)" in line:
        skip = False
        new_lines.append("            }\n")
        new_lines.append(line)
    elif not skip:
        new_lines.append(line)

with open("app/src/main/java/com/example/ui/PowerLogApp.kt", "w") as f:
    f.writelines(new_lines)
