# Part 3 Assignment 3

# Part 1. Correlation between GDP and access to personal computers in the year of 2005
# Question 1. Is there a relationship between GDP/capita and the number of personal computers per 100 individuals measured in 2005?

install.packages("rmarkdown")
library(rmarkdown)

# Checking data
View(GDP_PC)
str(GDP_PC)
typeof(GDP_PC)

# Converting character to numeric
attach(GDP_PC)
gdp_per_capita <- as.numeric(GDPperCAPITA)
gdp_per_capita
typeof(gdp_per_capita)

pc_per_100 <- as.numeric(PC_per_100)
pc_per_100
typeof(pc_per_100)
       
# 6. Calculating mean, median and standard deviation
summary(gdp_per_capita)
summary(pc_per_100)
sd(gdp_per_capita)
sd(pc_per_100)

# GDP per capita
# Mean: 7963.5
# Median: 2196.2
# SD: 12328.81

# PC per 100
# Mean: 15.781
# Median: 5.9
# SD: 22.12

# 7. Now make a plot where you can view if the variables are approximately normally distributed. Based on this choose the appropriate test (Pearson product-moment correlation or Spearman Rank-Order correlation)

hist(gdp_per_capita)
hist(pc_per_100)

plot(pc_per_100 ~ gdp_per_capita)

# The variables do not seem to be approximately normally distributed according to the histograms. 
# The mean and the median are far apart and the observations are not evenly distributed along the trend line.
# Therefore the Spearman's rank correlation is better to use.

# 8. Make an appropriate analysis and show the results as statistic values in the form: Correlation coefficient, df, p-value, name of test. Also, make a scatterplot to show the relationship.

cor.test(gdp_per_capita, pc_per_100, method = "spearman")

# Spearman's rank correlation rho
# Correlation coefficient rho: 0.85
# df: Not reported but I believe it is n - 2 = 155 - 2 = 153
# p-value: < 0.01
# There is a significant positive correlation between GDP per capita and PC_per_100

detach(GDP_PC)

# Part 2. Regression analysis
# Question 2
# Has the electricity generation per capita in China increased from 1990 to 2005?

# Checking data
attach(El_China)
El_China
View(El_China)
str(El_China)
typeof(El_China)

# 3. Make a regression

model <- lm(El_China$El_China ~ El_China$Year)
model
summary(model)
# R2 is 0.88
# p-value for F is < 0.01
# p-value for Year is < 0.01

# 4. Before interpreting the results, check if the model is well adapted to the dataset, using the diagnostic tools (basic diagnostic plots), and a scatterplot.
# Does the relationship look linear?

plot(El_China$El_China ~ El_China$Year,
     xlab = "Year", ylab = "Electricity generation per person (kilowatt-hours")
abline(model)

# Looking at the scatterplot it can be seen that the observations are not evenly distributed around the regression line.
# There seem to be a pattern.

par(mfrow=c(2,2))
plot(model)

# The plot with fitted values against residuals show a parabola and the model does not seem to capture a linear relationship.
# This could be a sign of heteroskedasticity, i.e. non-constant variance in the residuals.

# The Q-Q residuals plot shows appromximately normally distributed residuals since most of the points follow a straight line.

# There does not seem to be a problem with influential outliers according to the Residuas vs Leverage plot.


# 6. Transform your response variable using log10 transformation. Evaluate the result using diagnostic diagrams and the R2 value.

model_2 <- lm(log10(El_China) ~ Year, data = El_China)
model_2
summary(model_2)
# R2 is 0.97
# p-value for F is < 0.01
# p-value for Year is < 0.01

par(mfrow=c(1,1))
plot(log10(El_China$El_China) ~ El_China$Year,
     xlab = "Year", ylab = "Log of Electricity generation per person (kilowatt-hours")
abline(model_2)
# The scatterplot looks a bit better, but there still seem to be a pattern.

par(mfrow=c(2,2))
plot(model_2)
# There still seem to be a problem with heteroscedasticity and now the normality assumption can also be questioned.

# 7. Transform your response variable using square root transformation; sqrt() in R. Evaluate the result using diagnostic diagrams and the R2 value.

model_3 <- lm(sqrt(El_China) ~ Year, data = El_China)
model_3
summary(model_3)
# R2 is 0.93
# p-value for F is < 0.01
# p-value for Year is < 0.01

par(mfrow=c(1,1))
plot(sqrt(El_China$El_China) ~ El_China$Year,
     xlab = "Year", ylab = "Square root of Electricity generation per person (kilowatt-hours")
abline(model_3)
# Comparing with the other scatterplots the square root transformation of Y seem to be the second best choice.
# The log10 transformation have the points closer to the regression line, but square root transformation seem to be better than the original model.

par(mfrow=c(2,2))
plot(model_3)
# With square root transformation the normality looks better than for log10 transformation.
# But the residuals vs fitted show heteroscedasticity.

# 8. Compare all the different models you created using the results from the diagnostic tools as well as the R2 values from the models. Clearly motivate which model you find best to use for the final interpretation. Interpret the results from the model you choose to use! When you report the result from a linear regression you need to report the statistical values for b (regression coefficient), SEb , t , df and p. Visualize the result with a scatterplot including a least square line. make sure that the axis labels are informative and correct.

# The model with log10 transformation have is scattered closest to the regression line.
# All models show a parabola shape in the residuals vs fitted plot which is a sign of heteroscedasticity.
# Regarding the normality assumption the best choices seem to be the original model or the square root transformation model.

# All models have significant p-values for F and for t.
# The log10 transformation has the highest R2 with 0.97, second comes the square root transformation with 0.93 and the original model has 0.88.

# I would choose the model with square root transformation since the assumption of normally distributed residuals seem to hold for that model.
# R2 of 0.93 is very high and clearly good enough. 93% of the variance in electricity capacity per capita can be "explained" by the variable year.
# However, there is probably something else behind the real explanation since time cannot produce anything in itself.

# The best model
# Square root of Electricity = -2.914e+03 + 1.475e+00Year 

# b: 1.475e+00 
# SEb: 9.678e-02
# t-value for Year-coefficient: 15.24
# df: 17
# p-value for Year-coefficient: 2.40e-11
# We can see some very small values in these results

# The scatterplot with the least squares line is shown again
par(mfrow=c(1,1))
plot(sqrt(El_China$El_China) ~ El_China$Year,
     xlab = "Year", ylab = "Square root of Electricity generation per person (kilowatt-hours")
abline(model_3)



# Part 3. Testing differences between groups

# Question 3. Is there a difference in income between the New York districts, Manhattan and Brooklyn?

# 1. Create a histogram to evaluate the distribution of the income data.
# Based on the distribution, which test is most appropriate to use?
library(ggplot2)

ggplot(Lander_HousingNew, aes(x=Income)) + geom_histogram()

# It does not look approximately normally distributed, therefore the Wilcoxon's rank-sum test can be used.
# There is a tail to the right.

# 2. Is there a significant difference in income?
# Do a two-samples t-test (also called independent samples t-test) or Wilcoxon rank-sum test for independent data, to test for the difference in income between the two districts, Manhattan and Brooklyn. Interpret the result!
# What are the measured central tendencies for income in the two districts?
# How large is the estimated difference in income, and is the difference significant?

# This would be the test if the income data had been normally distributed
t.test(Income ~ Boro, data = Lander_HousingNew, var.equal = TRUE)
# There is a significant different in mean income between Brooklyn and Manhattan

attach(Lander_HousingNew)
wilcox.test(Income ~ Boro, alternative = "two.sided")
detach(Lander_HousingNew)
# There is a significant different in mean income between Brooklyn and Manhattan. The p-value is very low.

# The mean in Brooklyn is 639 548.1 and in Manhattan 3 344 961.0

# Calculate the median, mean and SD for both variables.

# First I am separating the data set in two subsets. One for Brooklyn and one for Manhattan.
brooklyn_data <- subset(Lander_HousingNew, Boro == "Brooklyn")
manhattan_data <- subset(Lander_HousingNew, Boro == "Manhattan")

summary(brooklyn_data$Income)
# The mean income in Brooklyn is 639 548 and the median is 443 851.

summary(manhattan_data$Income)
# The mean income in Manhattan is 3 344 961 and the median is 1 742 515.

sd(brooklyn_data$Income)
# The standard deviation in Brooklyn is 598 728.4

sd(manhattan_data$Income)
# The standard deviation in Manhattan is 4 345 457.

# 3. Make an appropriate graph that displays your findings properly. Choose the most appropriate diagram for this type of data matching the test you used.

par(mfrow=c(1,1))
boxplot(brooklyn_data$Income, manhattan_data$Income, xlab="Group", ylab = "Income", main = "Income in Brooklyn (left) and Manhattan(right)", outline=FALSE)

# 4. Interpret the results from the test you used! Remember to always present the test statistic t for t-test, v for Wilcoxon’s test, degrees of freedom df, p-value and what test you used. If you cannot find df in the output of the test, use the number of observations n.

# I made two tests
# The first two sample t-test showed
# t: -2.8983
# df: 77
# p-value: 0.004885
# There is a significant different in mean income between Brooklyn and Manhattan.

# The second test Wilcoxon rank sum test
# Which is better to choose because we could not assume normally distributed data for income
# W: 223
# p-value: 1.019e-05 (which is very small)
# There is a significant different in mean income between Brooklyn and Manhattan.


# Question 4
# Are there differences in house pricing (SEK/m2) in Sweden between 2016 and 2017?
# Paired samples test of median or mean difference among treatments

# 1. Create a histogram to evaluate the distribution of the income data. Based on the distribution, decide which test that is most appropriate to use. Give a justification for your choice.

attach(Housepricing_sweden)
hist(X2016_sek_sqrm)
hist(X2017_sek_sqrm)

# In this case we can do a paired t-test. There seems to be approximatley a normal distribution for sek per square meter for 2016 and 2017.

# 2. Perform a paired samples test for the difference in price (SEK/m2) between the two years 2016 and 2017.
# Interpret the results!
# Is there a significant difference?
# Explain why we use a paired samples test for this type of data!

t.test(X2017_sek_sqrm, X2016_sek_sqrm, paired = TRUE)

# There is a significant mean difference between 2016 and 2017. The p-value is 0.002885

# We use a paired samples test because there are repeated measurements between two years.

# What are the the central tendencies for house pricing for 2016 and 2017, and what is the size of the difference in house pricing between the two years?

# The mean difference is 1848. The mean price per square meter in 2016 is 38 128.5 and  in 2017 39 976.5 sek.

mean_2016 <- mean(X2016_sek_sqrm)
mean_2017 <- mean(X2017_sek_sqrm)
year <- c("2016", "2017")
values <- c(mean_2016, mean_2017)

# 3. Make a plot displaying the difference, using e.g. the code provided in the exercise for t-tests.

barplot(height = values, names.arg = year, ylim = c(0, 50000), ylab = "SEK per square meter", main = "Square meter prices for houses in Sweden in 2016 and 2017")

# 4. Interpret the results from the test you choose to use! Remember to always present the test statistic (t for t-test, v for Wilcoxon), degrees of freedom df, p-value and what test you used. If you cannot find df in the output of the test, use the number of observations n.

# Paired t-test
# t-value: 3.8116
# df: 11
# p-value: 0.002885
# Significant difference in square meter prices between 2016 and 2017 in Sweden.
# Null hypothesis of no difference is rejected.