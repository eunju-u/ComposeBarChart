# BarCart
### showYAxisUnit == false

   showBarValue == true    |       showBarValue == false        |
:-------------------------:|:-------------------------:
 ![image](https://github.com/user-attachments/assets/3a5c7f81-c5ab-4792-a20e-84ddc853fce7)  |  ![image](https://github.com/user-attachments/assets/c6a22ec7-eddb-49a5-a2d8-be38a3aefb3f)


### showYAxisUnit == true
   showBarValue == true    |       showBarValue == false        |
:-------------------------:|:-------------------------:
 ![image](https://github.com/user-attachments/assets/f9279ef5-2cb1-4ced-8106-1859050db6ec)  |   ![image](https://github.com/user-attachments/assets/8e12e06c-8763-43de-a0bb-afb9f85fd550)





  
# Getting Started / Installation

        dependencies {
                implementation("io.github.eunju-u/compose-bar-chart:1.0.1”)
        }




# Usage
+ BarCartWidget parameters
  
      /**
       * @param gridLineSpacing sets the spacing between the horizontal lines drawn in the background of the chart.
       * @param gridLineStrokeWidth sets the thickness of the horizontal lines drawn in the background of the chart.
       * @param showYAxisUnit sets the visibility of the unit numbers on the Y-axis.
       * @param yMax sets the maximum value of the Y-axis. It is only available for Type B of the bar chart.
       * @param showBarValue sets the visibility of the value text displayed inside each bar.
       * @param yUnit sets the unit of the Y-axis.
       * @param barWidth sets the width of the bars.
       * @param yTextStyle sets the text style for the Y-axis, including properties like text size, color, and font.
       * @param barTextStyle sets the text style for the text displayed on the bars, including properties like text size, color, and font.
       * It is only available for Type A of the bar chart.
       * @param color sets the color of the bars. If there are two or more colors, a gradient will be applied.
       * @param shape sets the corner shape of the top of the bars.
       * @param list sets the values that determine the height of the bars.
       *
       * **/
      @Composable
      fun BarCartWidget(
          barChartType: BarChartType = BarChartType.A,
          gridLineSpacing: Dp = 30.dp,
          gridLineStrokeWidth: Dp = 1.dp,
          showYAxisUnit: Boolean = false,
          showBarValue: Boolean = false,
          yMax: Int = 25, // Y-axis maximum (Only need B type)
          yUnit: Int = 5, // Y-axis unit
          barWidth: Dp = 20.dp,
          yTextStyle: TextStyle = TextStyle(),
          barTextStyle: TextStyle = TextStyle(), //(Only need A type)
          color: List<Color> = listOf(Color.Yellow, Color.Cyan),
          shape: RoundedCornerShape = RoundedCornerShape(0.dp),
          list: List<GraphItem>,
      ) {
        }



    
# maven central repositot

https://central.sonatype.com/artifact/io.github.eunju-u/compose-bar-chart/1.0.1/dependencies

