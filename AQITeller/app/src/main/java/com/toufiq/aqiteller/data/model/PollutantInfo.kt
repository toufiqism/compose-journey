data class PollutantInfo(
    val name: String,
    val fullName: String,
    val description: String,
    val healthEffects: String,
    val sources: String
)

object PollutantDetails {
    val CO = PollutantInfo(
        name = "CO",
        fullName = "Carbon Monoxide",
        description = "A colorless, odorless gas that is produced by incomplete combustion of carbon-containing materials.",
        healthEffects = "Can cause headaches, dizziness, and nausea. High levels can be fatal as it reduces oxygen delivery to the body's organs.",
        sources = "Vehicle exhaust, industrial processes, and indoor sources like gas stoves and heating systems."
    )

    val NO2 = PollutantInfo(
        name = "NO2",
        fullName = "Nitrogen Dioxide",
        description = "A reddish-brown gas with a pungent odor, part of a group of gaseous air pollutants called nitrogen oxides (NOx).",
        healthEffects = "Can irritate airways, aggravate respiratory diseases, particularly asthma, and contribute to respiratory infections.",
        sources = "Vehicle emissions, power plants, industrial facilities, and off-road equipment."
    )

    val O3 = PollutantInfo(
        name = "O3",
        fullName = "Ozone",
        description = "A gas composed of three oxygen atoms. While beneficial in the upper atmosphere, it's harmful at ground level.",
        healthEffects = "Can trigger chest pain, coughing, throat irritation, and congestion. Worsens bronchitis, emphysema, and asthma.",
        sources = "Created by chemical reactions between NOx and VOCs in sunlight. Major sources include industrial facilities and vehicle exhaust."
    )

    val SO2 = PollutantInfo(
        name = "SO2",
        fullName = "Sulphur Dioxide",
        description = "A colorless gas with a sharp, pungent odor. Part of a larger group of sulfur oxide gases.",
        healthEffects = "Can harm the respiratory system, make breathing difficult, and aggravate existing heart disease.",
        sources = "Fossil fuel combustion at power plants, industrial facilities, and volcanic eruptions."
    )

    val PM25 = PollutantInfo(
        name = "PM2.5",
        fullName = "Fine Particulate Matter",
        description = "Tiny particles or droplets in the air that are 2.5 microns or less in width.",
        healthEffects = "Can penetrate deep into lungs and bloodstream, causing respiratory issues, heart problems, and other serious health effects.",
        sources = "Vehicle emissions, power plants, wood burning, and industrial processes."
    )

    val PM10 = PollutantInfo(
        name = "PM10",
        fullName = "Coarse Particulate Matter",
        description = "Inhalable particles with diameters of 10 micrometers or less.",
        healthEffects = "Can cause respiratory issues, aggravate asthma, and contribute to cardiovascular problems.",
        sources = "Dust from construction sites, landfills, agriculture, wildfires, and roads."
    )
} 