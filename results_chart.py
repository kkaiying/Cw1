import matplotlib.pyplot as plt

nfz_sizes = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
close_avg = [3.00, 3.86, 4.74, 6.10, 11.66, 9.24, 11.32, 13.04, 35.10, 97.48, 24.90]
far_avg = [29.00, 36.50, 49.14, 84.46, 79.96, 157.76, 134.22, 131.12, 152.92, 194.06, 261.16]
# close_avg = [3.00, 2.26, 10.18, 9.18, 9.94, 9.42, 6.88, 9.42, 11.80, 8.76, 14.62]
# far_avg = [29.00, 22.40, 88.72, 153.78, 115.98, 94.46, 67.74, 82.26, 82.22, 89.76, 111.40]

x = range(len(nfz_sizes))
width = 0.35

fig, ax = plt.subplots(figsize=(10, 6))
ax.bar([i - width/2 for i in x], close_avg, width, label='Close')
ax.bar([i + width/2 for i in x], far_avg, width, label='Far')

ax.axhline(y=200, color='black', linestyle='--', linewidth=1.5)

ax.set_xlabel('NFZ Database Size')
ax.set_ylabel('Average Time (ms)')
ax.set_title('findPath() Average Runtime by NFZ Database Size')
ax.set_xticks(x)
ax.set_xticklabels(nfz_sizes)
ax.legend()
plt.tight_layout()
plt.show()