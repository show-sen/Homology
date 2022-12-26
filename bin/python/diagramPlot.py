import matplotlib.pyplot as plt

root_path = "C:\\Users\\tomoy\\Documents\\Projects\\"
data_path = "Delaunay\\data\\"
output_path  = "Delaunay\\output\\"
output_file_name = "Diagram.png"
edge_file_name = "Edge.dat"
point_file_name = "01.dat"

with open(root_path+output_path+edge_file_name) as f:
    edge_list = f.readlines()

with open(root_path+data_path+point_file_name) as f:
    point_list = f.readlines()


for item in edge_list:
    points = item.split(" ")
    plt.plot([float(points[0]), float(points[2])],[float(points[1]), float(points[3])],color="black")

for item in point_list:
    coordinates = item.split(" ")
    plt.plot(float(coordinates[0]), float(coordinates[1]),color= "black", marker='.', markersize=20)



plt.axis("off")
plt.savefig(root_path+output_path+output_file_name)
